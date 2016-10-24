package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy.Criteria;
import com.epam.java.rt.lab.dao.sql.Update.SetValue;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.entity.business.Comment;
import com.epam.java.rt.lab.entity.business.Comment.Property;
import com.epam.java.rt.lab.entity.business.Photo;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.PropertyManager;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.util.file.UploadManager;
import com.epam.java.rt.lab.web.component.Page;
import com.epam.java.rt.lab.web.validator.ValidatorFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.epam.java.rt.lab.entity.business.Photo.NULL_PHOTO;
import static com.epam.java.rt.lab.exception.AppExceptionCode.NULL_NOT_ALLOWED;
import static com.epam.java.rt.lab.service.ServiceExceptionCode.PHOTO_FILE_ACCESS_ERROR;
import static com.epam.java.rt.lab.util.PropertyManager.*;

public class CommentService extends BaseService {

    public List<Comment> getCommentList(Page page, Long applicationId)
            throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(Property.APPLICATION_ID, Predicate.PredicateOperator.EQUAL,
                        applicationId));
        page.setCountItems(dao(Comment.class.getSimpleName()).
                count(daoParameter));
        daoParameter.setOrderByCriteriaArray(Criteria.desc(Property.CREATED));
        daoParameter.setLimit((page.getCurrentPage() - 1)
                * page.getItemsOnPage(), page.getItemsOnPage());
        return dao(Comment.class.getSimpleName()).read(daoParameter);
    }

    public List<Comment> getCommentListByUser(Page page, Long userId)
            throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(Property.USER_ID, Predicate.PredicateOperator.EQUAL,
                        userId));
        page.setCountItems(dao(Comment.class.getSimpleName()).
                count(daoParameter));
        daoParameter.setOrderByCriteriaArray(Criteria.desc(Property.CREATED));
        daoParameter.setLimit((page.getCurrentPage() - 1)
                * page.getItemsOnPage(), page.getItemsOnPage());
        return dao(Comment.class.getSimpleName()).read(daoParameter);
    }

    public int updateComment(Comment comment) throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setSetValueArray(
                new SetValue(Property.CREATED, comment.getCreated()),
                new SetValue(Property.USER_ID, comment.getUser().getId()),
                new SetValue(Property.APPLICATION_ID,
                        comment.getApplicationId()),
                new SetValue(Property.PHOTO_ID, comment.getPhotoId()),
                new SetValue(Property.MESSAGE, comment.getMessage()));
        daoParameter.setWherePredicate(Predicate.
                get(Property.ID, Predicate.PredicateOperator.EQUAL,
                        comment.getId()));
        return dao(Comment.class.getSimpleName()).update(daoParameter);
    }

    public Long addComment(Comment comment) throws AppException {
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setEntity(comment);
        return dao(Comment.class.getSimpleName()).create(daoParameter);
    }

    public Photo getPhoto(String id) throws AppException {
        if (ValidatorFactory.getInstance().
                create(ValidatorFactory.DIGITS).validate(id).length > 0) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(Property.ID, Predicate.PredicateOperator.EQUAL, id));
        List<Photo> photoList = dao(Photo.class.getSimpleName()).
                read(daoParameter);
        if (photoList == null || photoList.size() == 0) return NULL_PHOTO;
        return photoList.get(0);
    }

    public void setCommentPhoto(Comment comment, String photoPath)
            throws AppException {
        if (photoPath != null) {
            String[] pair = photoPath.split(PropertyManager.ESCAPED_QUESTION);
            if (pair.length == 2) pair = pair[1].split(EQUAL);
            if (PATH.equals(pair[0]))
                comment.setPhotoId(addPhoto(pair[1]));
        }
    }

    public Long addPhoto(String filePath) throws AppException {
        if (filePath == null) throw new AppException(NULL_NOT_ALLOWED);
        int backslashLastIndex = filePath.lastIndexOf(ESCAPED_BACKSLASH);
        String fileName = filePath.substring(backslashLastIndex + 1);
        String[] metaInfo = UploadManager.
                getMetaInfoFromPrefix(fileName, PHOTO_UPLOAD_TYPE);
        String photoName = metaInfo[0];
        String contentType = metaInfo[1];
        File file = new File(filePath);
        try (InputStream inputStream = new FileInputStream(file)) {
            Photo photo = new Photo();
            photo.setName(photoName);
            photo.setType(contentType);
            photo.setFile(inputStream);
            photo.setModified(TimestampManager.getCurrentTimestamp());
            DaoParameter daoParameter = new DaoParameter();
            daoParameter.setEntity(photo);
            return dao(Photo.class.getSimpleName()).create(daoParameter);
        } catch (IOException e) {
            String[] detailArray = {filePath};
            throw new AppException(PHOTO_FILE_ACCESS_ERROR,
                    e.getMessage(), e.getCause(), detailArray);
        }

    }

}