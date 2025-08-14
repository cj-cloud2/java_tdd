
import unittest
from unittest.mock import MagicMock
from app.devops_script import create_s3_bucket

class TestS3BucketCreation(unittest.TestCase):

    def test_create_bucket_calls_once_with_correct_args(self):
        mock_s3_client = MagicMock()
        mock_s3_client.create_bucket.return_value = {"ResponseMetadata": {"HTTPStatusCode": 200}}
        bucket_name = "my-test-bucket"

        response = create_s3_bucket(mock_s3_client, bucket_name)

        mock_s3_client.create_bucket.assert_called_once_with(Bucket=bucket_name)
        self.assertEqual(response["ResponseMetadata"]["HTTPStatusCode"], 200)

    def test_create_bucket_raises_for_invalid_name(self):
        mock_s3_client = MagicMock()
        mock_s3_client.create_bucket.side_effect = Exception("Invalid bucket name")

        with self.assertRaises(Exception) as context:
            create_s3_bucket(mock_s3_client, "Invalid_Bucket_Name!")

        self.assertIn("Invalid bucket name", str(context.exception))
        mock_s3_client.create_bucket.assert_called_once_with(Bucket="Invalid_Bucket_Name!")

    def test_create_bucket_called_multiple_times(self):
        mock_s3_client = MagicMock()
        mock_s3_client.create_bucket.return_value = {"ResponseMetadata": {"HTTPStatusCode": 200}}

        buckets = ["bucket-one", "bucket-two", "bucket-three"]
        for b in buckets:
            create_s3_bucket(mock_s3_client, b)

        self.assertEqual(mock_s3_client.create_bucket.call_count, 3)
        mock_s3_client.create_bucket.assert_any_call(Bucket="bucket-one")
        mock_s3_client.create_bucket.assert_any_call(Bucket="bucket-two")
        mock_s3_client.create_bucket.assert_any_call(Bucket="bucket-three")

if __name__ == "__main__":
    unittest.main()