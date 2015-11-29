public class ClusterEvaluation 
{

    /*
     * Calculate separation between two clusters given:
     *  float distance- the distance btwn each radius
     *  float r1, r2 - the respective standard deviations
     */
    public float clusterSeparation(float distance, float r1, float r2) {
        return distance - r1 - r2;
    }

    /*
     * Calculate separation between two clusters given:
     *  float[] c1, c2 - n-dimensional centers of each cluster
     *  float r1, r2 - the respective standard deviations
     */
    public float clusterSeparation(float[] c1, float[] c2, float r1, float r2) {
        return distance(c1, c2) - r1 - r2;
    }

    /*
     * Calculate the distance between two n-dimensional points, c1 and c2
     */
    private float distance(float[] c1, float[] c2) {
        float n = c1.length;
        float sqrSum = 0;
        for (int i = 0; i < n; i++) {
            sqrSum += (c1[i] - c2[i]) * (c1[i] - c2[i]);
        }
        return (float) Math.sqrt(sqrSum);
    }

    /*
     * Calculate the cohesion of a cluster given:
     *  int numMembers - the number of members in the cluster
     *  float r - the standard deviation of the cluster
     */
    public float cohesion(int numMembers, float r) {
        return (float) Math.PI * r * r / numMembers;
    }
}