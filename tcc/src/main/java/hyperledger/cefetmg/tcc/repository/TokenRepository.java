package hyperledger.cefetmg.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hyperledger.cefetmg.tcc.models.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long>{
	
}
