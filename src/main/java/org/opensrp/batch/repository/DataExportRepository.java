package org.opensrp.batch.repository;

import org.opensrp.batch.entity.Export;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataExportRepository extends JpaRepository<Export, Long> {
	
}
