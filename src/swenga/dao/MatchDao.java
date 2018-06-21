package swenga.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
 
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import swenga.model.MatchesModel;

@Repository
@Transactional
public class MatchDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
 
	public List<MatchesModel> getMatches() {
 
		TypedQuery<MatchesModel> typedQuery = entityManager.createQuery(
				"select m from MatchesModel m", MatchesModel.class);
		List<MatchesModel> typedResultList = typedQuery.getResultList();
		return typedResultList;
	}

}
