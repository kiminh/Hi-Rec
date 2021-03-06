/**
 * 
 */
package metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import interfaces.ListEvaluation;
import model.DataModel;
import model.Globals;
import model.Item;
import model.User;

/**
 * @author FBM
 *
 */
public class PopularityOnHit
implements ListEvaluation
{

    private DataModel trainData;
    private float meanPopulairtyValue = 0;
    private int n = 0;
    /* (non-Javadoc)
     * @see interfaces.ListEvaluation#addRecommendations(model.User, java.util.Map)
     */
    @Override
    public
    void addRecommendations(
            User user, Map<Integer, Float> list)
    {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
        if (list == null) {
            throw new IllegalArgumentException("Recommended list is null");
        }
        int listLengthThreshold = 0;
        final List<Integer> hitList = new ArrayList<>();
        for (final Entry<Integer, Float> entry: list.entrySet()) {
            if (listLengthThreshold>=Globals.AT_N) {
                break;
            }
            if (user.getItemRating().containsKey(entry.getKey())) {
                if (user.getItemRating().get((int)entry.getKey()) >= Globals.MINIMUM_THRESHOLD_FOR_POSITIVE_RATING) {
                    hitList.add(entry.getKey());
                }
            }
            listLengthThreshold++;
        }
        if(hitList.isEmpty()){
            return;
        }
        float sum = 0;
        for(int i=0;i<hitList.size();i++){
            sum+=populairty(hitList.get(i));
        }
        meanPopulairtyValue +=sum/(hitList.size()*1.0);
        n++;
    }

    /* (non-Javadoc)
     * @see interfaces.ListEvaluation#getEvaluationResult()
     */
    @Override
    public
    float getEvaluationResult() {
        
        final float result = (float)(meanPopulairtyValue/n*1.0);
        if(Float.isNaN(result)){
            return 0;
        }
        return result;
    }

    /**
     * @param itemId
     * @return
     */

    float populairty(
            Integer itemId)
    {
        final Item item = trainData.getItem(itemId);
        if(item==null){
            throw new IllegalStateException("ITEM COULD NOT BE NULL");
        }
        final float allUsers = trainData.getUsers().size();
        final float users = item.getUserRated().size();
        return (float)(users/allUsers);
    }

    /*
     * (non-Javadoc)
     * @see interfaces.ListEvaluation#setTrainData(model.DataModel)
     */
    @Override
    public
    void setTrainData(
            DataModel trainData)
    {
        this.trainData = trainData;
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public
            int hashCode() {
        return 10;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public
            boolean equals(
                    Object obj)
    {
        if (this.toString().equals(obj.toString())) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public
            String toString() {
        return "PopularityOnHit";
    }
    
}
