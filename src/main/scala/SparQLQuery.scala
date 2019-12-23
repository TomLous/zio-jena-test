import org.apache.jena.query.{Query, QueryFactory}

case class SparQLQuery(query: String) {
  lazy val render: Query = QueryFactory.create(query)
}
