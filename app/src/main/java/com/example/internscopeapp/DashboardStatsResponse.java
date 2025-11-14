//package com.example.internscopeapp;
//
//public class DashboardStatsResponse {
//    private int purchasedJobs;
//    private int usedJobs;
//    private int availableJobs;
//    private int totalApplied;
//
//    // Getters
//    public int getPurchasedJobs() { return purchasedJobs; }
//    public int getUsedJobs() { return usedJobs; }
//    public int getAvailableJobs() { return availableJobs; }
//    public int getTotalApplied() { return totalApplied; }
//
//    public void setPurchasedJobs(int purchasedJobs) {
//        this.purchasedJobs = purchasedJobs;
//    }
//
//    public void setUsedJobs(int usedJobs) {
//        this.usedJobs = usedJobs;
//    }
//
//    public void setAvailableJobs(int availableJobs) {
//        this.availableJobs = availableJobs;
//    }
//
//    public void setTotalApplied(int totalApplied) {
//        this.totalApplied = totalApplied;
//    }
//}
//

package com.example.internscopeapp;

import com.google.gson.annotations.SerializedName;

public class DashboardStatsResponse {

    @SerializedName("totalPurchasedJobs")
    private int purchasedJobs;

    @SerializedName("totalUsedJobs")
    private int usedJobs;

    @SerializedName("availableJobs")
    private int availableJobs;

    private int totalApplied; // calculated in app

    public int getPurchasedJobs() {
        return purchasedJobs;
    }

    public int getUsedJobs() {
        return usedJobs;
    }

    public int getAvailableJobs() {
        return availableJobs;
    }

    public int getTotalApplied() {
        return totalApplied;
    }

    public void setTotalApplied(int totalApplied) {
        this.totalApplied = totalApplied;
    }
}
