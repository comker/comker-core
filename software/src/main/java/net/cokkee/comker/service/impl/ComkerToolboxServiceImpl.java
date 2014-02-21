package net.cokkee.comker.service.impl;

import com.google.gson.Gson;
import java.util.Calendar;
import java.util.Date;
import net.cokkee.comker.service.ComkerToolboxService;

/**
 *
 * @author drupalex
 */
public class ComkerToolboxServiceImpl implements ComkerToolboxService {

    private Gson gson = new Gson();

    @Override
    public String convertEntityToJson(Object entity) {
        return gson.toJson(entity);
    }

    @Override
    public Date getTime() {
        return Calendar.getInstance().getTime();
    }

    @Override
    public long getTimeInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public Date getNextDate(int count) {
        Calendar currentCal = Calendar.getInstance();
        Calendar nextCal = (Calendar) currentCal.clone();
        nextCal.add(Calendar.DAY_OF_YEAR, count);
        return nextCal.getTime();
    }


}
