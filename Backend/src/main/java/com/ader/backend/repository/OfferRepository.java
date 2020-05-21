package com.ader.backend.repository;

import com.ader.backend.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query(nativeQuery = true,
            value = "select o.* from offers o " +
                    "join bids b on b.offer_id = o.id " +
                    "join users u on u.id = b.user_id " +
                    "where u.email = ?1 " +
                    "and not o.offer_status = 'EXPIRED'")
    List<Offer> findAllByUser_EmailAndBidsExist(String userEmail);

    @Query(nativeQuery = true,
            value = "select o.* from offers o " +
                    "join users u on u.id = o.author_id " +
                    "where u.email = ?1")
    List<Offer> findAllByAuthor_Email(String userEmail);

    @Query(nativeQuery = true,
            value = "select o.* from offers o " +
                    "join users u on u.id = o.assignee_id " +
                    "where u.email = ?1 " +
                    "and not o.offer_status = 'EXPIRED'")
    List<Offer> findAllByAssignedUser(String userEmail);

    @Query(nativeQuery = true,
            value = "select o.* from offers o " +
                    "join users u on o.assignee_id = u.id " +
                    "where u.email = ?1 and " +
                    "o.offer_status = 'COMPLETED'")
    List<Offer> findAllCompletedForUser(String userEmail);

    @Query(nativeQuery = true,
            value = "select * from offers " +
                    "where expire_date < now() " +
                    "and not offer_status = ?1"
    )
    List<Offer> findAllByExpireDateAndOfferStatus(String offerStatus);

    @Query(nativeQuery = true,
            value = "select * from offers " +
                    "where offer_status = ?1")
    List<Offer> findAllByOfferStatus(String offerStatus);
}
