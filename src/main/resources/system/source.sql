INSERT INTO USERS(user_id,username,email,birthdate,password,role,logflag,deleteflag,old_login_id)
VALUES('zHXbzgONxRE2F8MG','System','bs.system.prepare@gmail.com','19981123','System123','ROLE_PRE',0,0,null)
-- PRE --
$2a$10$lX8u5cq6laJNOiFsmSQOIeCowbL/UKqrd/R5ELu601lsAPaeYdrf6
------------------------------------------------------------------------------------------------------------------------
DELETE FROM user_images;
DELETE FROM images;
DELETE FROM users;
------------------------------------------------------------------------------------------------------------------------
ALTER TABLE users
RENAME COLUMN user_id TO userId,
RENAME COLUMN birthdate TO birthDate,
RENAME COLUMN deleteflag TO deleteFlag,
RENAME COLUMN logflag TO lockFlag,
RENAME COLUMN old_login_id TO oldLoginId,
RENAME COLUMN username TO userName;

ALTER TABLE types
RENAME COLUMN type_id TO typeId,
RENAME COLUMN typename TO typeName;

ALTER TABLE images
RENAME COLUMN image_id TO imageId,
RENAME COLUMN imagepathid TO imagePathId,
RENAME COLUMN imagename TO imageName,
RENAME COLUMN imagepath TO imagePath,
RENAME COLUMN timeupload TO timeUpload,
RENAME COLUMN type_id TO typeId;

ALTER TABLE categories
RENAME COLUMN category_id TO categoryId,
RENAME COLUMN categoryname TO categoryName;

ALTER TABLE products
RENAME COLUMN product_id TO productId,
RENAME COLUMN deleteflag TO deleteFlag,
RENAME COLUMN oldsku TO oldSku;

ALTER TABLE user_images
RENAME COLUMN user_image_id TO userImageId,
RENAME COLUMN user_id TO userId,
RENAME COLUMN image_id TO imageId;

ALTER TABLE category_images
RENAME COLUMN category_image_id TO categoryImageId,
RENAME COLUMN category_id TO categoryId,
RENAME COLUMN image_id TO imageId;

ALTER TABLE product_images
RENAME COLUMN product_image_id TO productImageId,
RENAME COLUMN product_id TO productId,
RENAME COLUMN image_id TO imageId;

ALTER TABLE product_categories
RENAME COLUMN product_category_id TO productCategoryId,
RENAME COLUMN product_id TO productId,
RENAME COLUMN category_id TO categoryId;

ALTER TABLE files
DROP COLUMN type_id;

ALTER TABLE Payments
ADD COLUMN usingPoint boolean NOT NULL;
ADD COLUMN usingCash boolean NOT NULL;

