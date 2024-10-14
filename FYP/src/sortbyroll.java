import java.util.Comparator;

class SortbyDiff implements Comparator<toLLS> {
    // Used for sorting in ascending order of
    // roll number
    public int compare(toLLS a,toLLS b)
    {
        return (int) (a.diff*1000 - b.diff*1000);
    }
}
class SortbyAlphaD implements Comparator<toLLS>
{
	@Override
	public int compare(toLLS a, toLLS b) {
		// TODO Auto-generated method stub
		return (int) (a.Alpha*1000-b.Alpha*1000);
	}
	
}