package com.example.testcontainers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

public class MongoDbTest {

    @Container
    final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    private MongoClient mongoClient;

    @BeforeEach
    public void setUp() {
        mongoDBContainer.start();
        mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl());
    }

    @AfterEach
    public void tearDown() {
        mongoDBContainer.stop();
    }

    @Test
    public void testSimplePutAndGet() {
        assertThat(mongoClient.listDatabases().iterator().next().get("name")).isEqualTo("admin");
    }
}
