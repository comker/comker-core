package net.cokkee.comker.service;

import java.util.HashMap;
import java.util.Map;
import net.cokkee.comker.model.ComkerPager;

/**
 *
 * @author drupalex
 */
public interface ComkerSessionService {

    Map<String, Object> getUserListCriteria();

    ComkerPager getUserListPager();

    Map<String, Object> getCrewListCriteria();

    ComkerPager getCrewListPager();
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static class Impl implements ComkerSessionService {

        private ComkerUserDetailsService userDetailsService;

        public ComkerUserDetailsService getUserDetailsService() {
            return userDetailsService;
        }

        public void setUserDetailsService(ComkerUserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        //----------------------------------------------------------------------
        
        private Map<String,Object> userListCriteria = new HashMap<String, Object>();

        @Override
        public Map<String, Object> getUserListCriteria() {
            return userListCriteria;
        }
        
        private ComkerPager userListPager = new ComkerPager();

        @Override
        public ComkerPager getUserListPager() {
            return userListPager;
        }

        //----------------------------------------------------------------------

        private Map<String,Object> crewListCriteria = new HashMap<String, Object>();

        @Override
        public Map<String, Object> getCrewListCriteria() {
            return crewListCriteria;
        }

        private ComkerPager crewListPager = new ComkerPager();

        @Override
        public ComkerPager getCrewListPager() {
            return crewListPager;
        }
    }
}
