package ro.trenulmeu.mobile.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "ro.trenulmeu.mobile.models");
        schema.enableKeepSectionsByDefault();

        Entity dataBaseStatus = createStatus(schema);
        Entity operator = createOperators(schema);
        Entity trainType = createTypes(schema);
        Entity trainService = createServices(schema);
        Entity trainPrice = createPrices(trainType, operator, schema);
        Entity station = createStations(schema);
        Entity train = createTrains(trainType, trainService, operator, station, schema);
        Entity availability = createAvailability(train, schema);
        Entity path = createPath(train, station, schema);

        new DaoGenerator().generateAll(schema, "models/src/main/java");
    }

    private static Entity createStatus(Schema schema) {
        Entity entity = schema.addEntity("DataBaseStatus");

        entity.setTableName("DataBaseStatus");
        entity.addIdProperty().columnName("Id").autoincrement();
        entity.addDateProperty("Date").columnName("Date");
        entity.addBooleanProperty("NeedDeploy").columnName("NeedDeploy");
        entity.addDateProperty("ValidFrom").columnName("ValidFrom");
        entity.addDateProperty("ValidTo").columnName("ValidTo");

        return entity;
    }

    private static Entity createOperators(Schema schema) {
        Entity entity = schema.addEntity("TrainOperator");

        entity.setTableName("TrainOperator");
        entity.addIdProperty().columnName("Id");
        entity.addStringProperty("Name").columnName("Name");

        return entity;
    }

    private static Entity createTypes(Schema schema) {
        Entity entity = schema.addEntity("TrainType");

        entity.setTableName("TrainType");
        entity.addIdProperty().columnName("Id");
        entity.addStringProperty("Name").columnName("Name");
        entity.addStringProperty("LongName").columnName("LongName");

        return entity;
    }

    private static Entity createServices(Schema schema) {
        Entity entity = schema.addEntity("TrainService");

        entity.setTableName("TrainService");
        entity.addIdProperty().columnName("Id");
        entity.addStringProperty("Name").columnName("Name");

        return entity;
    }

    private static Entity createPrices(Entity type, Entity operator, Schema schema) {
        Entity entity = schema.addEntity("TrainPrice");

        entity.setTableName("TrainPrice");
        entity.addIdProperty().columnName("Id");
        Property priceType = entity.addLongProperty("TypeId").columnName("").getProperty();
        Property priceOperator = entity.addLongProperty("OperatorId").columnName("").getProperty();
        entity.addStringProperty("Service").columnName("Service");
        entity.addIntProperty("KmFrom").columnName("KmFrom");
        entity.addIntProperty("KmTo").columnName("KmTo");
        entity.addFloatProperty("Price").columnName("Price");
        entity.addToOne(type, priceType, "TrainType");
        entity.addToOne(operator, priceOperator, "Operator");

        return entity;
    }

    private static Entity createStations(Schema schema) {
        Entity entity = schema.addEntity("Station");

        entity.setTableName("Station");
        entity.addIdProperty().columnName("Id");
        entity.addStringProperty("Name").columnName("Name");
        entity.addFloatProperty("Lat").columnName("Lat");
        entity.addFloatProperty("Lon").columnName("Lon");
        entity.addIntProperty("TimeOffset").columnName("TimeOffset");

        return entity;
    }

    private static Entity createTrains(Entity type, Entity service, Entity operator, Entity station, Schema schema) {
        Entity entity = schema.addEntity("Train");

        entity.setTableName("Train");
        entity.addIdProperty().columnName("Id");
        Property trainOperator = entity.addLongProperty("OperatorId").columnName("OperatorId").getProperty();
        Property trainTypeP = entity.addLongProperty("TypeId").columnName("TypeId").getProperty();
        Property trainServiceP = entity.addLongProperty("ServiceId").columnName("ServiceId").getProperty();
        entity.addStringProperty("Name").columnName("Name");
        entity.addStringProperty("OriginalName").columnName("OriginalName");
        Property trainFrom = entity.addLongProperty("FromId").columnName("FromId").getProperty();
        Property trainTo = entity.addLongProperty("ToId").columnName("ToId").getProperty();
        entity.addStringProperty("FromName").columnName("FromName");
        entity.addStringProperty("ToName").columnName("ToName");
        entity.addShortProperty("FromTime").columnName("FromTime")
                .customType("ro.trenulmeu.mobile.timespan.TimeSpan", "ro.trenulmeu.mobile.timespan.TimeSpanAdapter");
        entity.addShortProperty("ToTime").columnName("ToTime")
                .customType("ro.trenulmeu.mobile.timespan.TimeSpan", "ro.trenulmeu.mobile.timespan.TimeSpanAdapter");
        entity.addShortProperty("TotalTime").columnName("TotalTime")
                .customType("ro.trenulmeu.mobile.timespan.TimeSpan", "ro.trenulmeu.mobile.timespan.TimeSpanAdapter");
        entity.addToOne(operator, trainOperator, "Operator");
        entity.addToOne(type, trainTypeP, "TrainType");
        entity.addToOne(service, trainServiceP, "TrainService");
        entity.addToOne(station, trainFrom, "From");
        entity.addToOne(station, trainTo, "To");
        operator.addToMany(entity, trainOperator, "Trains");
        type.addToMany(entity, trainTypeP, "Trains");
        service.addToMany(entity, trainServiceP, "Trains");

        return entity;
    }

    private static Entity createAvailability(Entity train, Schema schema) {
        Entity entity = schema.addEntity("TrainAvailability");

        entity.setTableName("TrainAvailability");
        entity.addIdProperty().columnName("Id").autoincrement();
        Property availabilityTrain = entity.addLongProperty("TrainId").columnName("TrainId").getProperty();
        entity.addDateProperty("From").columnName("From");
        entity.addDateProperty("To").columnName("To");
        entity.addStringProperty("Days").columnName("Days");
        entity.addToOne(train, availabilityTrain, "Train");
        train.addToMany(entity, availabilityTrain, "Availability");

        return entity;
    }

    private static Entity createPath(Entity train, Entity station, Schema schema) {
        Entity entity = schema.addEntity("TrainPath");

        entity.setTableName("TrainPath");
        entity.addIdProperty().columnName("Id").autoincrement();
        Property pathTrain = entity.addLongProperty("TrainId").columnName("TrainId").getProperty();
        Property pathKm = entity.addFloatProperty("Km").columnName("Km").getProperty();
        Property pathStation = entity.addLongProperty("StationId").columnName("StationId").getProperty();
        entity.addShortProperty("Arrive").columnName("Arrive")
                .customType("ro.trenulmeu.mobile.timespan.TimeSpan", "ro.trenulmeu.mobile.timespan.TimeSpanAdapter");
        entity.addShortProperty("Depart").columnName("Depart")
                .customType("ro.trenulmeu.mobile.timespan.TimeSpan", "ro.trenulmeu.mobile.timespan.TimeSpanAdapter");
        entity.addIntProperty("Stationary").columnName("Stationary");
        entity.addBooleanProperty("IsStop").columnName("IsStop");
        entity.addIntProperty("Speed").columnName("Speed");
        entity.addToOne(train, pathTrain, "Train");
        entity.addToOne(station, pathStation, "Station");
        train.addToMany(entity, pathTrain, "Path").orderAsc(pathKm);

        return entity;
    }

}
