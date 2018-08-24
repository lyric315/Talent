package sks.talent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lirui on 2018/8/24.
 */

public class PermissionUtils {

    public static void checkPermissions(Activity context, @NonNull final Runnable action) {
        int permissionWRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRECORD = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
        int permissionMOUNT_FILESYSTEMS = ContextCompat.checkSelfPermission(context, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionRECORD != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECORD_AUDIO);
        }
        if (permissionMOUNT_FILESYSTEMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        }

        if (listPermissionsNeeded.isEmpty()) {
            //已获得权限
            action.run();
            return;
        }

        //申请权限
        XXPermissions.with(context)
                .permission(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            action.run();
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });
    }
}
