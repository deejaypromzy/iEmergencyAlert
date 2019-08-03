package com.prince.dj.iemergencyalert;

public interface PermissionResult {

    void permissionGranted();

    void permissionDenied();

    void permissionForeverDenied();
}
