package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy.Criteria;
import com.epam.java.rt.lab.dao.sql.Where.Predicate;
import com.epam.java.rt.lab.dao.sql.WherePredicateOperator;
import com.epam.java.rt.lab.entity.access.User;
import com.epam.java.rt.lab.entity.business.Comment;
import com.epam.java.rt.lab.entity.business.CommentProperty;
import com.epam.java.rt.lab.entity.business.Photo;
import com.epam.java.rt.lab.entity.business.PhotoProperty;
import com.epam.java.rt.lab.exception.AppException;
import com.epam.java.rt.lab.util.TimestampManager;
import com.epam.java.rt.lab.util.file.UploadManager;
import com.epam.java.rt.lab.web.component.FormControlValue;
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

    public List<Comment> getCommentList(Long applicationId)
            throws AppException {
        if (applicationId == null) throw new AppException(NULL_NOT_ALLOWED);
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(CommentProperty.APPLICATION_ID,
                        WherePredicateOperator.EQUAL, applicationId));
        daoParameter.setOrderByCriteriaArray(Criteria.
                desc(CommentProperty.CREATED));
        return dao(Comment.class.getSimpleName()).read(daoParameter);
    }

    public boolean addComment(User user, Long applicationId,
                              FormControlValue photoValue,
                              FormControlValue messageValue)
            throws AppException {
        if (user == null || applicationId == null
                || photoValue == null || messageValue == null) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        String photoPath = null;
        String[] pair = photoValue.getValue().split(ESCAPED_QUESTION);
        if (pair.length == 2) {
            pair = pair[1].split(EQUAL);
            if (PATH.equals(pair[0])) {
                // photo uploaded
                photoPath = pair[1];
            }
        }
        if (photoPath == null && (messageValue.getValue() == null
                || messageValue.getValue().length() == 0)) {
            return false;
        }
        Comment comment = new Comment();
        comment.setCreated(TimestampManager.getCurrentTimestamp());
        comment.setUser(user);
        comment.setApplicationId(applicationId);
        if (photoPath != null) comment.setPhotoId(addPhoto(photoPath));
        comment.setMessage(messageValue.getValue());
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setEntity(comment);
        dao(Comment.class.getSimpleName()).create(daoParameter);
        return true;
    }

    public Photo getPhoto(String id) throws AppException {
        if (ValidatorFactory.getInstance().
                create(ValidatorFactory.DIGITS).validate(id).length > 0) {
            throw new AppException(NULL_NOT_ALLOWED);
        }
        DaoParameter daoParameter = new DaoParameter();
        daoParameter.setWherePredicate(Predicate.
                get(PhotoProperty.ID, WherePredicateOperator.EQUAL, id));
        List<Photo> photoList = dao(Photo.class.getSimpleName()).
                read(daoParameter);
        if (photoList == null || photoList.size() == 0) return NULL_PHOTO;
        return photoList.get(0);
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