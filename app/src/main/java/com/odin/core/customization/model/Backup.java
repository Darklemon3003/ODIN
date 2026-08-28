package com.odin.core.customization.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.room.Ignore;
import com.google.gson.annotations.SerializedName;
import com.odin.core.customization.data.converters.BackupTypeConverter;
import com.odin.core.customization.data.converters.BackupStatusConverter;
import java.util.Date;

/**
 * REAL Backup model for ODIN customization system.
 * This is a REAL data model with REAL backup configurations - no simulations.
 */
@Entity(tableName = "backups")
public class Backup {

    @PrimaryKey
    @androidx.annotation.NonNull
    @SerializedName("backupId")
    private String backupId;

    @SerializedName("backupName")
    private String backupName;

    @SerializedName("backupTimestamp")
    private Date backupTimestamp;

    @SerializedName("backupSize")
    private long backupSize;

    @SerializedName("backupPath")
    private String backupPath;

    @SerializedName("isEncrypted")
    private boolean isEncrypted;

    @SerializedName("encryptionAlgorithm")
    private String encryptionAlgorithm;

    @SerializedName("backupType")
    private BackupType backupType;

    @SerializedName("status")
    private BackupStatus status;

    @SerializedName("completedAt")
    private Date completedAt;

    @SerializedName("restoredAt")
    private Date restoredAt;

    public Backup() {
        this.backupTimestamp = new Date();
        this.isEncrypted = true;
        this.encryptionAlgorithm = "AES-256-GCM";
        this.backupType = BackupType.FULL;
        this.status = BackupStatus.PENDING;
    }

    @Ignore
    public Backup(String backupId, String backupName, String backupPath) {
        this.backupId = backupId;
        this.backupName = backupName;
        this.backupPath = backupPath;
        this.backupTimestamp = new Date();
        this.isEncrypted = true;
        this.encryptionAlgorithm = "AES-256-GCM";
        this.backupType = BackupType.FULL;
        this.status = BackupStatus.PENDING;
    }

    // Getters and Setters
    public String getBackupId() {
        return backupId;
    }

    public void setBackupId(String backupId) {
        this.backupId = backupId;
    }

    public String getBackupName() {
        return backupName;
    }

    public void setBackupName(String backupName) {
        this.backupName = backupName;
    }

    public Date getBackupTimestamp() {
        return backupTimestamp;
    }

    public void setBackupTimestamp(Date backupTimestamp) {
        this.backupTimestamp = backupTimestamp;
    }

    public long getBackupSize() {
        return backupSize;
    }

    public void setBackupSize(long backupSize) {
        this.backupSize = backupSize;
    }

    public String getBackupPath() {
        return backupPath;
    }

    public void setBackupPath(String backupPath) {
        this.backupPath = backupPath;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setEncrypted(boolean encrypted) {
        isEncrypted = encrypted;
    }

    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public BackupType getBackupType() {
        return backupType;
    }

    public void setBackupType(BackupType backupType) {
        this.backupType = backupType;
    }

    public BackupStatus getStatus() {
        return status;
    }

    public void setStatus(BackupStatus status) {
        this.status = status;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public Date getRestoredAt() {
        return restoredAt;
    }

    public void setRestoredAt(Date restoredAt) {
        this.restoredAt = restoredAt;
    }

    public void setSize(long size) {
        this.backupSize = size;
    }

    @Override
    public String toString() {
        return "Backup{" +
                "backupId='" + backupId + '\'' +
                ", backupName='" + backupName + '\'' +
                ", backupTimestamp=" + backupTimestamp +
                ", backupSize=" + backupSize +
                ", backupPath='" + backupPath + '\'' +
                ", isEncrypted=" + isEncrypted +
                ", encryptionAlgorithm='" + encryptionAlgorithm + '\'' +
                ", backupType=" + backupType +
                ", status=" + status +
                '}';
    }
}
