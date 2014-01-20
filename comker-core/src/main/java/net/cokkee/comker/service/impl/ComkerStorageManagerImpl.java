package net.cokkee.comker.service.impl;

import java.util.ArrayList;
import java.util.List;
import net.cokkee.comker.model.po.ComkerAnswer;
import net.cokkee.comker.model.po.ComkerQuestion;
import net.cokkee.comker.service.ComkerStorageManager;

/**
 *
 * @author drupalex
 */
public class ComkerStorageManagerImpl extends ComkerStorageManager {

    private List<ComkerQuestion> questionList = new ArrayList<ComkerQuestion>();

    public ComkerStorageManagerImpl() {
        questionList.add(new ComkerQuestion(
                "Lorem ipsum dolor sit amet, reque probatus suavitate eum in. Illum tation efficiendi vim ei. Soleat virtute his cu, apeirian pertinacia per ne?",
                "Vel ne modus eruditi, mea an diam omnium repudiandae, ut nam cibo aperiam.",
                new ComkerAnswer[] {
                    new ComkerAnswer("Facete singulis eam no, magna aliquip", 1, ""),
                    new ComkerAnswer("Ea qui sale justo aliquando.", 0, ""),
                    new ComkerAnswer("Sea postea vocibus at", 0, ""),
                    new ComkerAnswer("Dicam dicunt pertinacia quo ex, tota appetere", 0, "")
                }
        ));
        questionList.add(new ComkerQuestion(
                "Te pri dicam noster inermis. Ne munere perfecto dissentias mea, nisl salutandi his ne?",
                "Putant viderer philosophia sit ei.",
                new ComkerAnswer[] {
                    new ComkerAnswer("Repudiare forensibus ne vel.", 1, ""),
                    new ComkerAnswer("Quo in semper causae tacimates.", 0, ""),
                    new ComkerAnswer("Quot tincidunt eloquentiam eu pri.", 0, ""),
                    new ComkerAnswer("Ei per disputando philosophia.", 0, "")
                }
        ));
    }

    @Override
    public List<ComkerQuestion> getQuestionList() {
        return questionList;
    }
}
