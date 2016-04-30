/** Natasha Bornhorst nbornho1 Richard Ding rding2 Karl Tayeb ktayeb1 */

/** returns distance. 
 *  @param <T> t. */
public interface Distance<T> {
    /** returns distance. 
 *      @param one one.
 *      @param two two. 
 *      @return double distance. */
    double distance(T one, T two);
}
