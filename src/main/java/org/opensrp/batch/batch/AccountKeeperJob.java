package org.opensrp.batch.batch;

import java.io.IOException;
import java.util.Random;

import javax.sql.DataSource;

import org.opensrp.batch.entity.Users;
import org.opensrp.batch.repository.UsersRepository;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AccountKeeperJob extends JobExecutionListenerSupport {
	
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
	
	@Autowired
	private UsersRepository repo;
	
	public String param;
	
	@Bean(name = "accountJob")
	public Job accountKeeperJob() {
		
		Step step = stepBuilderFactory.get("step-1").<Users, Users> chunk(1).reader(reader()).processor(processor)
		        .writer(writer()).build();
		
		Job job = jobBuilderFactory.get(System.currentTimeMillis() + "").incrementer(new RunIdIncrementer()).listener(this)
		        .start(step).build();
		System.err.println("JobId:" + job.getJobParametersIncrementer());
		//this.param = job.getJobParametersIncrementer().getNext("");
		return job;
	}
	
	public JdbcCursorItemReader<Users> reader() {
		
		JdbcCursorItemReader<Users> reader = new JdbcCursorItemReader<Users>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT id, name FROM customer");
		reader.setRowMapper(new UserRowMapper());
		
		return reader;
	}
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.err.println("befores:" + jobExecution.getJobParameters().getString("query"));
		this.param = jobExecution.getJobParameters().getString("query");
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			Users user = new Users();
			user.setUserId(new Random().nextLong());
			user.setDept("mohit");
			user.setName("Ali");
			repo.save(user);
			System.err.println("URS:" + user);
			System.err.println("BATCH JOB COMPLETED SUCCESSFULLY");
		}
	}
	
	public FlatFileItemWriter<Users> writer() {
		FlatFileItemWriter<Users> writer = new FlatFileItemWriter<Users>();
		try {
			resource = resource.createRelative("file:/opt/" + new Random().toString() + ".csv");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.setResource(resource);
		writer.setLineAggregator(new DelimitedLineAggregator<Users>() {
			
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<Users>() {
					
					{
						setNames(new String[] { "name" });
					}
				});
			}
		});
		
		return writer;
	}
}
