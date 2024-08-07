package com.ensas.librarymanagementsystem.repositories;

import com.ensas.librarymanagementsystem.Model.security.Authority;
import com.ensas.librarymanagementsystem.Model.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, UUID> {

    Authority findByPermission(String borrowBook);


    void deleteById(UUID authorityId);

    List<Authority> findAllByIdIn(Set<String> authorities);



    @Query("SELECT a FROM Authority a WHERE a.name IN :names")
    List<Authority> findAllByName(@Param("names") List<String> names);

    Optional<Authority> findByName(String name);

}
