package com.coinwise.repository;

import com.coinwise.model.Group;
import com.coinwise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
    
    @Query("SELECT DISTINCT g FROM Group g WHERE g.createdBy = :user OR :user MEMBER OF g.members")
    List<Group> findUserGroups(@Param("user") User user);
}
