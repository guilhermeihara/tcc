package hyperledger.cefetmg.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hyperledger.cefetmg.tcc.models.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>{
	
}
