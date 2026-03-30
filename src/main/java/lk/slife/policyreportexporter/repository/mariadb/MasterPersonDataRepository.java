package lk.slife.policyreportexporter.repository.mariadb;

import lk.slife.policyreportexporter.entity.mariadb.MasterPersonDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterPersonDataRepository extends JpaRepository<MasterPersonDataEntity, Integer> {

}
