package de.westnordost.streetcomplete.quests.opening_hours;

import android.os.Bundle;

import de.westnordost.streetcomplete.data.QuestImportance;
import de.westnordost.streetcomplete.data.osm.OverpassQuestType;
import de.westnordost.streetcomplete.quests.AbstractQuestAnswerFragment;
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder;

import de.westnordost.streetcomplete.R;


public class AddOpeningHours extends OverpassQuestType
{
    @Override protected String getTagFilters()
	{
		return " nodes, ways, relations with ( shop and shop !~ no|vacant or" +
               " amenity ~ restaurant|cafe|ice_cream|fast_food|bar|pub|biergarten|food_court|cinema|nightclub|" +
                          "bank|atm|bureau_de_change|money_transfer|post_office|library|courthouse|embassy|" +
                          "car_wash|car_rental|marketplace|fuel|driving_school|" +
		                  "dentist|doctors|clinic|pharmacy|veterinary or" +
               " amenity = bicycle_parking and bicycle_parking = building or" +
               " amenity = parking and parking = multi-storey or" +
               " amenity = recycling and recycling_type = centre or" +
               " tourism ~ zoo|aquarium|theme_park|gallery|museum or" +
               " tourism = information and information = office or" +
               " leisure ~ golf_course|water_park|miniature_golf|dance|bowling_alley|horse_riding" +
						  "sports_centre|fitness_centre|amusement_arcade|adult_gaming_centre|tanning_salon )" +
               " and !opening_hours and name";
	}
	
    @Override public int importance()
    {
        return QuestImportance.MINOR;
    }

	@Override public AbstractQuestAnswerFragment createForm()
	{
		return new AddOpeningHoursForm();
	}

	@Override public Integer applyAnswerTo(Bundle answer, StringMapChangesBuilder changes)
	{
		String openingHours = answer.getString(AddOpeningHoursForm.OPENING_HOURS);
		if(openingHours != null)
		{
			changes.add("opening_hours", openingHours);
			return R.string.quest_openingHours_commitMessage;
		}
		return null;
	}

	@Override public String getIconName() {	return "opening_hours"; }
}
