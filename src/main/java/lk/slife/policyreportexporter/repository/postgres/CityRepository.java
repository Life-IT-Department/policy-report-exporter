package lk.slife.policyreportexporter.repository.postgres;

import lk.slife.policyreportexporter.entity.postgres.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

    @Query("SELECT c FROM CityEntity c JOIN FETCH c.electorate")
    List<CityEntity> findAllWithElectorate();
}
