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
	

	@Query(value = "SELECT media_id as id, COUNT(*) as count, SUM(lt.cost_incoming) as cost "
			+ "FROM user_like as ul JOIN like_type as lt on ul.like_type_id = lt.id "
			+ "WHERE media_id IN ?1 "
			+ "GROUP BY media_id", nativeQuery=true)
	List<LikesByMedia> countLikesByMedia(List<Long> mediaIds);
	

	@Query(value = "SELECT media_id as id, COUNT(*) as count, SUM(lt.cost_incoming) as cost "
			+ "FROM user_like as ul JOIN like_type as lt on ul.like_type_id = lt.id "
			+ "WHERE media_id IN ?1 AND ul.owner = ?2 "
			+ "GROUP BY media_id", nativeQuery=true)
	List<LikesByMedia> countLikesByMediaAndUser(List<Long> mediaIds, String username);
	
}
