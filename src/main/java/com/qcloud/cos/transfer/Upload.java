package com.qcloud.cos.transfer;

import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.exception.PauseException;
import com.qcloud.cos.model.UploadResult;

/**
 * Represents an asynchronous upload to Qcloud COS.
 * <p>
 * See {@link TransferManager} for more information about creating transfers.
 * </p>
 *
 * @see TransferManager#upload(String, String, java.io.File)
 * @see TransferManager#upload(com.qcloud.cos.model.PutObjectRequest)
 */
public interface Upload extends Transfer {

    /**
     * Waits for this upload to complete and returns the result of this upload.
     * This is a blocking call. Be prepared to handle errors when calling this
     * method. Any errors that occurred during the asynchronous transfer will be
     * re-thrown through this method.
     *
     * @return The result of this transfer.
     *
     * @throws CosClientException
     *             If any errors were encountered in the client while making the
     *             request or handling the response.
     * @throws CosServiceException
     *             If any errors occurred in Qcloud COS while processing the
     *             request.
     * @throws InterruptedException
     *             If this thread is interrupted while waiting for the upload to
     *             complete.
     */
    public UploadResult waitForUploadResult()
            throws CosClientException, CosServiceException, InterruptedException;

    /**
     * Pause the current upload operation and returns the information that can
     * be used to resume the upload.
     *
     * Upload cannot be paused in the following cases.
     * <ul>
     *  <li>The data source is an input stream.</li>
     *  <li>Client side encryption is used.</li>
     *  <li>Server Side Encryption with customer provided key is used.</li>
     *  <li>Size of the file being uploaded is less than the {@link TransferManagerConfiguration#getMultipartUploadThreshold()}.</li>
     * </ul>
     * In such cases, aborts the uploads and a <code>PauseFailure</code> exception
     * is thrown
     *
     * @return An opaque token that holds some private state and can be used to
     *         resume a paused download operation.
     *
     * @throws PauseException
     *             If failed to pause the operation.
     */
    public PersistableUpload pause() throws PauseException;

    /**
     * Tries to pause the current upload operation and returns the information
     * that can be used to resume the upload.
     *
     * Upload cannot be paused in the following cases.
     * <ul>
     *  <li>The data source is an input stream.</li>
     *  <li>Client side encryption is used.</li>
     *  <li>Server Side Encryption with customer provided key is used.</li>
     *  <li>Size of the file being uploaded is less than the {@link TransferManagerConfiguration#getMultipartUploadThreshold()}.</li>
     * </ul>
     *
     * In such cases, aborts the uploads if forceCancelTransfers is set else No
     * action is taken.
     *
     * @param forceCancelTransfers
     *            a boolean to forcefully abort the existing uploads if pause
     *            cannot be done.
     *
     * @return a result of pause operation.
     */
    public PauseResult<PersistableUpload> tryPause(boolean forceCancelTransfers);

    /**
     * Abort the current upload operation.
     */
    public void abort();

}