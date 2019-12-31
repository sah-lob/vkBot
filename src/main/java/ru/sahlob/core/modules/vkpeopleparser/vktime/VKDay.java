package ru.sahlob.core.modules.vkpeopleparser.vktime;

import java.util.*;

public class VKDay {

    private VKHour[] vkHours = new VKHour[24];

    public VKHour[] getVkHours() {
        return vkHours;
    }

    public void addVkHoursList(ArrayList<VKHour> parsingTime) {
        for (var h: parsingTime) {
            if (vkHours[h.getHour()] != null) {
                var originVkMinutes = vkHours[h.getHour()].getOnlineMinutes();
                h.getOnlineMinutes();

                for (int i = 0; i < originVkMinutes.length; i++) {
                    if (h.getOnlineMinutes()[i]) {
                        originVkMinutes[i] = true;
                    }
                }
                vkHours[h.getHour()].setOnlineMinutes(originVkMinutes);
            } else {
                vkHours[h.getHour()] = h;
            }
        }
    }

    public void addvkHour(VKHour vkHour) {
        vkHours[vkHour.getHour()] = vkHour;
    }
}