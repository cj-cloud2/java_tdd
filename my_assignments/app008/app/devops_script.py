
def create_s3_bucket(s3_client, bucket_name):
    """Creates an S3 bucket using provided boto3 S3 client."""
    return s3_client.create_bucket(Bucket=bucket_name)