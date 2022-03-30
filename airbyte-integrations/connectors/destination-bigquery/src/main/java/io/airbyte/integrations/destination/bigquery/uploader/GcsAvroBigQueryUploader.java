/*
 * Copyright (c) 2021 Airbyte, Inc., all rights reserved.
 */

package io.airbyte.integrations.destination.bigquery.uploader;

import com.google.cloud.bigquery.*;
import com.google.cloud.bigquery.JobInfo.WriteDisposition;
import io.airbyte.integrations.destination.bigquery.formatter.BigQueryRecordFormatter;
import io.airbyte.integrations.destination.gcs.GcsDestinationConfig;
import io.airbyte.integrations.destination.gcs.avro.GcsAvroWriter;

public class GcsAvroBigQueryUploader extends AbstractGscBigQueryUploader<GcsAvroWriter> {

  public GcsAvroBigQueryUploader(TableId table,
                                 TableId tmpTable,
                                 GcsAvroWriter writer,
                                 JobInfo.WriteDisposition syncMode,
                                 GcsDestinationConfig gcsDestinationConfig,
                                 BigQuery bigQuery,
                                 boolean isKeepFilesInGcs,
                                 BigQueryRecordFormatter recordFormatter) {
    super(table, tmpTable, writer, syncMode, gcsDestinationConfig, bigQuery, isKeepFilesInGcs, recordFormatter);
  }

  @Override
  protected LoadJobConfiguration getLoadConfiguration() {
    return LoadJobConfiguration.builder(tmpTable, writer.getFileLocation()).setFormatOptions(FormatOptions.avro())
        .setSchema(recordFormatter.getBigQuerySchema())
        // always append to the tmp table, because we are uploading the data in small batches
        .setWriteDisposition(WriteDisposition.WRITE_APPEND)
        .setUseAvroLogicalTypes(true)
        .build();
  }

}
