package com.swiftship.logistic.backend.services;

import com.swiftship.logistic.backend.models.entities.Test;
import com.swiftship.logistic.backend.repositories.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public List<Test> findAllUsers() {
        return this.testRepository.findAll();
    }

    public Test createTest(String name) {
        Test newTest = new Test(name);
        this.testRepository.save(newTest);
        return newTest;
    }
}
