package com.epam.java.rt.lab.service;

import com.epam.java.rt.lab.dao.DaoException;
import com.epam.java.rt.lab.dao.DaoParameter;
import com.epam.java.rt.lab.dao.sql.OrderBy;
import com.epam.java.rt.lab.dao.sql.Update;
import com.epam.java.rt.lab.dao.sql.Where;
import com.epam.java.rt.lab.entity.business.Category;
import com.epam.java.rt.lab.entity.business.Comment;
import com.epam.java.rt.lab.entity.business.Photo;
import com.epam.java.rt.lab.util.TimestampCompare;
import com.epam.java.rt.lab.util.validator.ValidatorException;
import com.epam.java.rt.lab.util.validator.ValidatorFactory;
import com.epam.java.rt.lab.web.component.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * category-ms
 */
public class CommentService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    public CommentService()
            throws ServiceException {
    }

    public List<Comment> getCommentList(Page page, Long applicationId) throws ServiceException {
        try {
            DaoParameter daoParameter = new DaoParameter().setWherePredicate(
                    Where.Predicate.get(
                            Comment.Property.APPLICATION_ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            applicationId
                    )
            );
            page.setCountItems(dao(Comment.class.getSimpleName()).count(daoParameter));
            daoParameter
                    .setOrderByCriteriaArray(OrderBy.Criteria.desc(
                            Comment.Property.CREATED
                    ))
                    .setLimit(
                            (page.getCurrentPage() - 1) * page.getItemsOnPage(),
                            page.getItemsOnPage()
                    );
            return dao(Comment.class.getSimpleName()).read(daoParameter);
        } catch (DaoException e) {
            throw new ServiceException("exception.service.comment.get-comment-list.dao", e.getCause());
        }
    }

    public List<Comment> getCommentListByUser(Page page, Long userId) throws ServiceException {
        try {
            DaoParameter daoParameter = new DaoParameter().setWherePredicate(
                    Where.Predicate.get(
                            Comment.Property.USER_ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            userId
                    )
            );
            page.setCountItems(dao(Comment.class.getSimpleName()).count(daoParameter));
            daoParameter
                    .setOrderByCriteriaArray(OrderBy.Criteria.desc(
                            Comment.Property.CREATED
                    ))
                    .setLimit(
                            (page.getCurrentPage() - 1) * page.getItemsOnPage(),
                            page.getItemsOnPage()
                    );
            return dao(Comment.class.getSimpleName()).read(daoParameter);
        } catch (DaoException e) {
            throw new ServiceException("exception.service.comment.get-comment-list.dao", e.getCause());
        }
    }

    public int updateComment(Comment comment) throws ServiceException {
        try {
            return dao(Comment.class.getSimpleName()).update(new DaoParameter()
                    .setSetValueArray(
                            new Update.SetValue(Comment.Property.CREATED, comment.getCreated()),
                            new Update.SetValue(Comment.Property.USER_ID, comment.getUser().getId()),
                            new Update.SetValue(Comment.Property.APPLICATION_ID, comment.getApplicationId()),
                            new Update.SetValue(Comment.Property.PHOTO_ID, comment.getPhotoId()),
                            new Update.SetValue(Comment.Property.MESSAGE, comment.getMessage())
                    )
                    .setWherePredicate(Where.Predicate.get(
                            Comment.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            comment.getId()
                    ))
            );
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.comment.update-comment.dao", e.getCause());
        }
    }

    public Long addComment(Comment comment) throws ServiceException {
        try {
            return dao(Comment.class.getSimpleName()).create(new DaoParameter().setEntity(comment));
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.comment.add-comment.dao", e.getCause());
        }
    }

    public Photo getPhoto(String id) throws ServiceException {
        try {
            if (ValidatorFactory.create("digits").validate(id) != null) return null;
            List<Photo> photoList = dao(Photo.class.getSimpleName()).read(new DaoParameter()
                    .setWherePredicate(Where.Predicate.get(
                            Photo.Property.ID,
                            Where.Predicate.PredicateOperator.EQUAL,
                            id
                    ))
            );
            if (photoList == null || photoList.size() == 0) return null;
            return photoList.get(0);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.comment.get-photo.dao", e.getCause());
        } catch (ValidatorException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCommentPhoto(Comment comment, String photoPath) throws ServiceException {
        if (photoPath != null) {
            String[] pair = photoPath.split("\\?");
            if (pair.length == 2) pair = pair[1].split("=");
            if ("path".equals(pair[0]))
                comment.setPhotoId(addPhoto(pair[1]));
        }
    }

    public Long addPhoto(String filePath) throws ServiceException {
        String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
        int photoInfoIndex = fileName.lastIndexOf(".photo.");
        String photoName = fileName.substring(0, photoInfoIndex);
        photoInfoIndex = photoInfoIndex + 8;
        String contentType = fileName.substring(photoInfoIndex,
                fileName.indexOf(".", photoInfoIndex)).replaceAll("_", "/");
        try (InputStream inputStream = new FileInputStream(new File(filePath))) {
            Photo photo = new Photo();
            photo.setName(photoName);
            photo.setType(contentType);
            photo.setFile((FileInputStream) inputStream);
            photo.setModified(TimestampCompare.getCurrentTimestamp());
            return dao(Photo.class.getSimpleName()).create(new DaoParameter().setEntity(photo));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.comment.add-photo.file", e.getCause());
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("exception.service.comment.add-photo.dao", e.getCause());
        }
    }

}
