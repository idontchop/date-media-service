package com.idontchop.datemediaservice.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.idontchop.datemediaservice.dtos.LikesByMedia;
import com.idontchop.datemediaservice.entities.Like;

public interface LikeRepository extends CrudRepository<Like,Long> {

	List<Like> findAllByOwner(String owner);
	Optional<Like> findByOwnerAndMedia_Id(String owner, long mediaId);
	Optional<Like> findByOwnerAndMedia_IdAndLikeType_Id(String owner, long mediaId, long likeTypeId);
	List<Like> findByOwnerAndMedia_IdIn(String owner, List<Long> mediaIds);
	List<Like> findAllByMedia_Id(long id);
	Long countByMedia_Id(long id);
	
	//@Query("SELECT l.media_id  FROM Like l WHERE l.media_id IN :mediaIds GROUP BY l.media_id")
	@Query(value = "SELECT media_id as id, COUNT(*) as count, SUM(user_like.cost) as cost"
			+ " FROM user_like WHERE media_id IN ?1 GROUP BY media_id", nativeQuery=true)
	List<LikesByMedia> countLikesByMedia(List<Long> mediaIds);
	
}
