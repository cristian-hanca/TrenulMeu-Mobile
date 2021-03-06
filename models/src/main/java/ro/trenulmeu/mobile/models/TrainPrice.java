package ro.trenulmeu.mobile.models;

import ro.trenulmeu.mobile.models.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "TrainPrice".
 */
public class TrainPrice {

    private Long id;
    private Long TypeId;
    private Long OperatorId;
    private String Service;
    private Integer KmFrom;
    private Integer KmTo;
    private Float Price;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TrainPriceDao myDao;

    private TrainType TrainType;
    private Long TrainType__resolvedKey;

    private TrainOperator Operator;
    private Long Operator__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public TrainPrice() {
    }

    public TrainPrice(Long id) {
        this.id = id;
    }

    public TrainPrice(Long id, Long TypeId, Long OperatorId, String Service, Integer KmFrom, Integer KmTo, Float Price) {
        this.id = id;
        this.TypeId = TypeId;
        this.OperatorId = OperatorId;
        this.Service = Service;
        this.KmFrom = KmFrom;
        this.KmTo = KmTo;
        this.Price = Price;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTrainPriceDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeId() {
        return TypeId;
    }

    public void setTypeId(Long TypeId) {
        this.TypeId = TypeId;
    }

    public Long getOperatorId() {
        return OperatorId;
    }

    public void setOperatorId(Long OperatorId) {
        this.OperatorId = OperatorId;
    }

    public String getService() {
        return Service;
    }

    public void setService(String Service) {
        this.Service = Service;
    }

    public Integer getKmFrom() {
        return KmFrom;
    }

    public void setKmFrom(Integer KmFrom) {
        this.KmFrom = KmFrom;
    }

    public Integer getKmTo() {
        return KmTo;
    }

    public void setKmTo(Integer KmTo) {
        this.KmTo = KmTo;
    }

    public Float getPrice() {
        return Price;
    }

    public void setPrice(Float Price) {
        this.Price = Price;
    }

    /** To-one relationship, resolved on first access. */
    public TrainType getTrainType() {
        Long __key = this.TypeId;
        if (TrainType__resolvedKey == null || !TrainType__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TrainTypeDao targetDao = daoSession.getTrainTypeDao();
            TrainType TrainTypeNew = targetDao.load(__key);
            synchronized (this) {
                TrainType = TrainTypeNew;
            	TrainType__resolvedKey = __key;
            }
        }
        return TrainType;
    }

    public void setTrainType(TrainType TrainType) {
        synchronized (this) {
            this.TrainType = TrainType;
            TypeId = TrainType == null ? null : TrainType.getId();
            TrainType__resolvedKey = TypeId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public TrainOperator getOperator() {
        Long __key = this.OperatorId;
        if (Operator__resolvedKey == null || !Operator__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TrainOperatorDao targetDao = daoSession.getTrainOperatorDao();
            TrainOperator OperatorNew = targetDao.load(__key);
            synchronized (this) {
                Operator = OperatorNew;
            	Operator__resolvedKey = __key;
            }
        }
        return Operator;
    }

    public void setOperator(TrainOperator Operator) {
        synchronized (this) {
            this.Operator = Operator;
            OperatorId = Operator == null ? null : Operator.getId();
            Operator__resolvedKey = OperatorId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
