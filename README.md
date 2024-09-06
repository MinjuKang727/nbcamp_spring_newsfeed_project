# 📳 뉴스피드 프로젝트 
팔로우한 사용자의 가장 최근 게시물들을 볼 수 있는 스프링 프로젝트

## 🤸‍♀️ 팀원
강민주, 김아름, 이민혁

## 🍃 사용한 기술
- Spring
- Jpa
- Spring data jpa
- Spring Security

## API 명세서
![image](https://github.com/user-attachments/assets/bd56520a-6e27-4690-80ea-1054f1c4f364)

## ERD 다이어그램
![image](https://github.com/user-attachments/assets/ded45167-bfee-445b-9c98-98ef5a7da9e9)

## 와이어프레임
![image](https://github.com/user-attachments/assets/83d440b2-ce0d-4e99-b7a7-3340e04e24d0)
![image](https://github.com/user-attachments/assets/c8cf577e-a835-4563-b9c8-db609186a127)
![image](https://github.com/user-attachments/assets/0f301410-c473-4a03-977b-26f19c6bc389)
![image](https://github.com/user-attachments/assets/73f50cef-80af-40ce-87d3-980bb1d2d4c8)
![image](https://github.com/user-attachments/assets/8aee9fbf-fb8d-41e1-b662-39dd66438410)
![image](https://github.com/user-attachments/assets/ea588b3f-f7d9-4989-8641-fab4ee411b76)
![image](https://github.com/user-attachments/assets/97f4ff59-6680-481e-af2f-0636ab5fe0d5)
![image](https://github.com/user-attachments/assets/493da0bb-60ce-4a1d-8461-5626a21f6682)
![image](https://github.com/user-attachments/assets/5efa8ae8-1908-4913-98ad-45c0fabbe172)
![image](https://github.com/user-attachments/assets/f1c3a4cd-22e0-44a6-89d5-ddebc0760d13)
![image](https://github.com/user-attachments/assets/3b48a373-6e56-4e97-a000-42d1ee6c7bc7)
![image](https://github.com/user-attachments/assets/dc2d5c4d-07dd-4926-b23d-9b22b7b1b2cc)
![image](https://github.com/user-attachments/assets/97dfbf43-d7e1-47b0-9e9d-0cd3b01ad77a)
![image](https://github.com/user-attachments/assets/684a7520-da27-4211-a8d1-3f0a06a3a92c)

## Flow Chart
### Main
![image](https://github.com/user-attachments/assets/8a79d96e-e766-4555-b233-e173c25654bd)

### User
![image](https://github.com/user-attachments/assets/f8a5c621-de29-4a5a-861b-5897f17ef68a)

### Post
![image](https://github.com/user-attachments/assets/abcc73e4-0584-48c0-8103-d652a8b9aed0)

### Comment
![image](https://github.com/user-attachments/assets/153d55c6-3ec4-4da3-a20d-4e64c86f4878)

### Friend
![image](https://github.com/user-attachments/assets/d331f553-87b0-44bd-861c-485af4dd1c48)

### Like
![image](https://github.com/user-attachments/assets/1238d95e-258b-4fee-8b12-7a790dc33c2a)


## 🏆 요구 기능

### **1. 프로필 관리**

- **프로필 조회 기능**
    - 다른 사용자의 프로필 조회 시, 민감한 정보는 표시되지 않습니다.
      
- **프로필 수정 기능**
    로그인한 사용자는 본인의 사용자 정보를 수정할 수 있습니다.
    
    - 비밀번호 수정 조건
        - 비밀번호 수정 시, 본인 확인을 위해 현재 비밀번호를 입력하여 올바른 경우에만 수정할 수 있습니다.
        - 현재 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다.
    - **⚠️ 예외처리**
        - 비밀번호 수정 시, 본인 확인을 위해 입력한 현재 비밀번호가 일치하지 않은 경우
        - 비밀번호 형식이 올바르지 않은 경우
        - 현재 비밀번호와 동일한 비밀번호로 수정하는 경우

### **2.  뉴스피드 게시물 관리**

- **게시물 작성, 조회, 수정, 삭제 기능**
    - 조건
        - 게시물 수정, 삭제는 작성자 본인만 처리할 수 있습니다.
    - **⚠️ 예외처리**
        - 작성자가 아닌 다른 사용자가 게시물 수정, 삭제를 시도하는 경우
- **뉴스피드 조회 기능**
    - 기본 정렬은 생성일자 기준으로 내림차순 정렬합니다.
    - 10개씩 페이지네이션하여, 각 페이지 당 뉴스피드 데이터가 10개씩 나오게 합니다.
    - **⚠️ 예외처리**
        - 다른 사람의 뉴스피드는 볼 수 없습니다.

### **3. 사용자 인증**

- **회원가입 기능**
    - 사용자 아이디
        - 사용자 아이디는 이메일 형식이어야 합니다.
    - 비밀번호
        - `Bcrypt`로 인코딩합니다.
            - 암호화를 위한 `PasswordEncoder`를 직접 만들어 사용합니다.
                        
        - 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함합니다.
        - 비밀번호는 최소 8글자 이상이어야 합니다.
    - **⚠️ 예외처리**
        - 중복된 `사용자 아이디`로 가입하는 경우
        - `사용자 아이디` 이메일과 비밀번호 형식이 올바르지 않은 경우
     
- **회원탈퇴 기능**
    
    회원 탈퇴 방식을 어떻게 처리할지 고민해보세요.
    
    - 조건
        - 탈퇴 처리 시 `비밀번호`를 확인한 후 일치할 때 탈퇴 처리합니다.
        - 탈퇴한 사용자의 아이디는 재사용할 수 없고, 복구할 수 없습니다.
    - **⚠️ 예외처리**
        - `사용자 아이디`와 `비밀번호`가 일치하지 않는 경우
        - 이미 탈퇴한 `사용자 아이디`인 경우
     
### **4. 친구 관리**

- 특정 사용자를 친추 추가/삭제 할 수 있습니다.
- 친구 기능이 구현되었다면, 뉴스피드에 친구의 최신 게시물들을 최신순으로 볼 수 있습니다.
- 친구는 상대방의 수락 기능이 필요합니다.

### **5. 업그레이드 뉴스피드**
- 정렬 기능
    - 수정일자 기준 최신순
    - 좋아요 많은 순
 
### **6. 댓글**
- 댓글 작성, 조회, 수정, 삭제
    - 사용자는 게시물에 댓글을 작성할 수 있고, 본인의 댓글은 **수정 및 삭제**를 할 수 있습니다.
    - **내용**만 수정이 가능합니다.
    - 댓글 수정, 삭제는 댓글의 작성자 혹은 게시글의 작성자만 가능합니다.
- 댓글 수정, 삭제는 댓글의 작성자 혹은 게시글의 작성자만 가능합니다.

### **7. 좋아요**
- **게시물 및 댓글 좋아요 / 좋아요 취소 기능**
    - 사용자가 게시물이나 댓글에 좋아요를 남기거나 취소할 수 있습니다.
    - 본인이 작성한 게시물과 댓글에 좋아요를 남길 수 없습니다.
    - 같은 게시물에는 사용자당 한 번만 좋아요가 가능합니다.
 
