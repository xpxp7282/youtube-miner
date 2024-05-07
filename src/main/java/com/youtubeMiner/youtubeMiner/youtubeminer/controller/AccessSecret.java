package com.youtubeMiner.youtubeMiner.youtubeminer.controller;
import com.google.cloud.secretmanager.v1.*;


public class AccessSecret {

    public static String getApiToken() throws Exception {
        // Replace with your project ID and secret name
        String projectId = "aiss-422607";
        String secretName = "YOUTUBE_API_KEY";

        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            // Build the resource name of the secret version
            String name = String.format("projects/%s/secrets/%s/versions/latest", projectId, secretName);
            // Access the secret version
            AccessSecretVersionResponse response = client.accessSecretVersion(name);
            // Get the secret payload (containing the token)
            String payload = response.getPayload().getData().toStringUtf8();
            return payload;
        }
    }
}
