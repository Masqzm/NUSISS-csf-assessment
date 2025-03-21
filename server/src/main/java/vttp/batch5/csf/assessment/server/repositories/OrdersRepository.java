package vttp.batch5.csf.assessment.server.repositories;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import vttp.batch5.csf.models.Menu;

@Repository
public class OrdersRepository {
  @Autowired
  private MongoTemplate template;

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  //  Native MongoDB query here
  //
  /*
      db.menus.aggregate(
          {
              $sort: { name: 1 }
          },
          {
              $project: {
                  id: '$_id',
                  name: 1,
                  description: 1,
                  price: 1,
                  _id: 0
              }
          }
      )
   */
  public List<Menu> getMenu() {
		SortOperation sortNameAsc = Aggregation.sort(Sort.Direction.ASC, "name");
    ProjectionOperation projectFields = Aggregation.project("name", "description", "price")
                                        .andExpression("_id").as("id");

		Aggregation pipeline = Aggregation.newAggregation(sortNameAsc, projectFields);
		
		List<Document> results = template.aggregate(pipeline, "menus", Document.class).getMappedResults();
		
		List<Menu> menus = results.stream()
                      .map(doc -> Menu.toMenu(doc))
                      .collect(Collectors.toList());
		
		return menus;
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  //  Native MongoDB query here
  
}
