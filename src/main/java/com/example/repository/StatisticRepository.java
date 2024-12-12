package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Statistic;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long>{

}
