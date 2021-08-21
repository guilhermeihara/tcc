package hyperledger.cefetmg.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hyperledger.cefetmg.tcc.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
}
