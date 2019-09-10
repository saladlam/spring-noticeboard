package info.saladlam.example.spring.noticeboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import info.saladlam.example.spring.noticeboard.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
