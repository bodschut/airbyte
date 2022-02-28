/*
 * Copyright (c) 2021 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.config.storage;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.airbyte.config.storage.CloudStorageConfigs.S3Config;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DefaultS3ClientFactoryTest {

  @Test
  public void testS3() {
    final var s3Config = Mockito.mock(S3Config.class);
    Mockito.when(s3Config.getBucketName()).thenReturn("test-bucket");
    Mockito.when(s3Config.getRegion()).thenReturn("us-east-1");
    new DefaultS3ClientFactory(s3Config).get();
  }

  @Test
  public void testS3RegionNotSet() {
    final var s3Config = Mockito.mock(S3Config.class);
    // Missing bucket and access key.
    Mockito.when(s3Config.getBucketName()).thenReturn("");
    Mockito.when(s3Config.getRegion()).thenReturn("");

    assertThrows(IllegalArgumentException.class, () -> new DefaultS3ClientFactory(s3Config));
  }

  @Test
  public void testIAMPropertiesFail() {
    final var s3Config = new S3Config("test-bucket", "us-east-1");

    // Calling these properties should intentionally fail for AWS S3. These should be fetched
    // via DefaultCredentialsProvider.
    assertThrows(IllegalArgumentException.class, () -> s3Config.getAwsAccessKey());
    assertThrows(IllegalArgumentException.class, () -> s3Config.getAwsSecretAccessKey());
  }

}
