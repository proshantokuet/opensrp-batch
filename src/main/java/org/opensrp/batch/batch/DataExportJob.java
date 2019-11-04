package org.opensrp.batch.batch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.opensrp.batch.dto.BranchDTO;
import org.opensrp.batch.dto.UserDTO;
import org.opensrp.batch.entity.DataExport;
import org.opensrp.batch.entity.Export;
import org.opensrp.batch.header.ChildHeader;
import org.opensrp.batch.header.FamilyHeader;
import org.opensrp.batch.header.MemberHeader;
import org.opensrp.batch.mapper.ChildRowMapper;
import org.opensrp.batch.mapper.HouseholdRowMapper;
import org.opensrp.batch.mapper.MemberRowMapper;
import org.opensrp.batch.repository.DataExportRepository;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;

@Configuration
@EnableBatchProcessing
@Component
public class DataExportJob extends JobExecutionListenerSupport implements StepExecutionListener {
	
	@Autowired
	JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	StepBuilderFactory stepBuilderFactory;
	
	@Value("${input.file}")
	Resource resource;
	
	@Autowired
	Processor processor;
	
	@Autowired
	WriterTM writer;
	
	@Autowired
	private DataSource dataSource;
	
	@Value("${household.form.name}")
	String householdFormName;
	
	@Value("${child.form.name}")
	String childFormName;
	
	@Value("${member.form.name}")
	String memberFormName;
	
	@Value("${file.dir}")
	String fileDir;
	
	@Autowired
	private DataExportRepository repo;
	
	@Autowired
	private FamilyHeader householdHeader;
	
	@Autowired
	private MemberHeader memberHeader;
	
	@Autowired
	private ChildHeader childHeader;
	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private UserDTO userDTO;
	
	@Autowired
	private BranchDTO branchDTO;
	
	@Bean(name = "accountJob")
	public Job accountKeeperJob() {
		
		Step step = stepBuilderFactory.get("step-1").<DataExport, DataExport> chunk(10)
		        .reader(reader(null, 0, null, null, null)).processor(processor).writer(writer(null, null)).build();
		
		Job job = jobBuilderFactory.get(System.currentTimeMillis() + "").incrementer(new RunIdIncrementer()).listener(this)
		        .start(step).build();
		System.err.println("JobId:" + job.getName());
		//this.param = job.getJobParametersIncrementer().getNext("");
		return job;
	}
	
	@Bean(name = "myJobLauncher")
	public JobLauncher simpleJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}
	
	@Bean
	@StepScope
	public JdbcCursorItemReader<DataExport> reader(@Value("#{jobParameters['query']}") String query,
	                                               @Value("#{jobParameters['branch']}") int branch,
	                                               @Value("#{jobParameters['formName']}") String formName,
	                                               @Value("#{jobParameters['user']}") String user,
	                                               @Value("#{jobParameters['userType']}") String userType) {
		
		int userId = userDTO.getUserIdByUserName(user);
		System.err.println("UserId:" + userId);
		List<Integer> branchList = branchDTO.getBranchByUser(userId);
		System.err.println("branchList:" + branchList);
		List<Integer> branches = new ArrayList<Integer>();
		//branches.add(9);
		branches.add(7);
		String sks = userDTO.getUserNames(branchList);
		System.err.println(sks);
		JdbcCursorItemReader<DataExport> reader = new JdbcCursorItemReader<DataExport>();
		reader.setDataSource(dataSource);
		query = query + " and provider_id in (" + sks + ")";
		reader.setSql(query);
		if (formName.equalsIgnoreCase(householdFormName)) {
			reader.setRowMapper(new HouseholdRowMapper());
			
		} else if (formName.equalsIgnoreCase(memberFormName)) {
			reader.setRowMapper(new MemberRowMapper());
		} else if (formName.equalsIgnoreCase(childFormName)) {
			reader.setRowMapper(new ChildRowMapper());
		} else {
			
		}
		
		return reader;
	}
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		String user = jobExecution.getJobParameters().getString("user");
		Export export = new Export();
		export.setJobId(jobExecution.getId());
		export.setId(jobExecution.getId());
		export.setCreatedDate(new Date());
		export.setCreator(user);
		export.setStatus("Processing");
		
		repo.save(export);
		System.err.println("URS:" + export);
		//System.err.println("befores:" + jobExecution.getJobParameters().getString("query"));
		//this.param = jobExecution.getJobParameters().getString("query");
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			Optional<Export> export = repo.findById(jobExecution.getId());
			
			String fileName = jobExecution.getJobParameters().getString("fileName");
			export.get().setJobId(jobExecution.getId());
			export.get().setFileName(fileName);
			export.get().setCreatedDate(new Date());
			export.get().setCreator("pro");
			export.get().setStatus("Processing");
			export.get().setStatus("Completed");
			repo.save(export.get());
			System.err.println("URS:" + export);
			System.err.println("BATCH JOB COMPLETED SUCCESSFULLY");
		}
	}
	
	@Bean
	@StepScope
	public FlatFileItemWriter<DataExport> writer(@Value("#{jobParameters['formName']}") String formName,
	                                             @Value("#{jobParameters['fileName']}") String fileName) {
		FlatFileItemWriter<DataExport> writer = new FlatFileItemWriter<DataExport>();
		try {
			resource = resource.createRelative(fileDir + fileName);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.setResource(resource);
		if (formName.equalsIgnoreCase(householdFormName)) {
			writer.setHeaderCallback(householdHeader);
		} else if (formName.equalsIgnoreCase(memberFormName)) {
			writer.setHeaderCallback(memberHeader);
		} else if (formName.equalsIgnoreCase(childFormName)) {
			writer.setHeaderCallback(childHeader);
		}
		
		writer.setLineAggregator(new DelimitedLineAggregator<DataExport>() {
			
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<DataExport>() {
					
					{
						if (formName.equalsIgnoreCase(householdFormName)) {
							setNames(new String[] { "Id", "householdNumber", "SSName", "villageName", "cluster",
							        "householdType", "name", "memberCount", "hasLatrine", "provider", "dateCreated" });
						} else if (formName.equalsIgnoreCase(memberFormName)) {
							setNames(new String[] { "Id", "memberNumber", "name", "relationwithHOH", "motherName",
							        "mobileNumber", "idType", "NIDNumber", "birthIdNumber", "DOBKnown", "dateofBirth",
							        "age", "gender", "maritalStatus", "bloodGroup", "provider", "dateCreated"/*, "guid"*/});
						} else if (formName.equalsIgnoreCase(childFormName)) {
							setNames(new String[] { "Id", "memberNumber", "name", "relationwithHOH", "motherName",
							        "dateofBirth", "gender", "bloodGroup", "provider", "dateCreated" });
						}
					}
				});
			}
		});
		
		return writer;
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
