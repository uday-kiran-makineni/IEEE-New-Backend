# IEEE Vardhaman Student Branch - Backend API

## Overview

This is the Spring Boot backend application for the IEEE Vardhaman Student Branch website. It provides RESTful APIs for managing societies, councils, events, achievements, notifications, gallery items, and more.

## Technology Stack

- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Data JPA**
- **Spring Security**
- **MySQL Database**
- **Lombok** for reducing boilerplate code
- **Maven** for dependency management

## Project Structure

```
demo/
├── src/main/java/com/example/demo/
│   ├── controller/          # REST Controllers
│   ├── dto/                 # Data Transfer Objects
│   ├── model/              # JPA Entities
│   ├── repository/         # JPA Repositories
│   ├── service/            # Business Logic Layer
│   └── security/           # Security Configuration
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## Database Schema

### Entities

1. **Society** - IEEE Societies (Computer Society, Signal Processing, etc.)
2. **Council** - IEEE Councils (EDA Council, RFID Council, etc.)
3. **PastEvent** - Completed events with feedback and statistics
4. **UpcomingEvent** - Future events with registration details
5. **Achievement** - Awards and recognitions
6. **GalleryItem** - Photo gallery with categorization
7. **Notification** - System notifications with priority levels
8. **HeroSlide** - Homepage carousel slides
9. **User** - User management and authentication
10. **EventRegistration** - Event registration tracking

## API Endpoints

### Societies
- `GET /api/societies` - Get all societies
- `GET /api/societies/active` - Get active societies
- `GET /api/societies/{id}` - Get society by ID
- `POST /api/societies` - Create new society
- `PUT /api/societies/{id}` - Update society
- `DELETE /api/societies/{id}` - Delete society

### Councils
- `GET /api/councils` - Get all councils
- `GET /api/councils/active` - Get active councils
- `GET /api/councils/{id}` - Get council by ID
- `POST /api/councils` - Create new council
- `PUT /api/councils/{id}` - Update council
- `DELETE /api/councils/{id}` - Delete council

### Past Events
- `GET /api/past-events` - Get all past events
- `GET /api/past-events/paged` - Get paginated past events
- `GET /api/past-events/{id}` - Get past event by ID
- `GET /api/past-events/search?keyword=` - Search past events
- `GET /api/past-events/statistics/by-year` - Get event statistics
- `POST /api/past-events` - Create new past event
- `PUT /api/past-events/{id}` - Update past event
- `DELETE /api/past-events/{id}` - Delete past event

### Upcoming Events
- `GET /api/upcoming-events` - Get all upcoming events
- `GET /api/upcoming-events/paged` - Get paginated upcoming events
- `GET /api/upcoming-events/{id}` - Get upcoming event by ID
- `GET /api/upcoming-events/open-registration` - Get events with open registration
- `GET /api/upcoming-events/free` - Get free events
- `GET /api/upcoming-events/type/{eventType}` - Get events by type
- `POST /api/upcoming-events` - Create new upcoming event
- `PUT /api/upcoming-events/{id}` - Update upcoming event
- `DELETE /api/upcoming-events/{id}` - Delete upcoming event

### Achievements
- `GET /api/achievements` - Get all achievements
- `GET /api/achievements/paged` - Get paginated achievements
- `GET /api/achievements/{id}` - Get achievement by ID
- `GET /api/achievements/featured` - Get featured achievements
- `GET /api/achievements/year/{year}` - Get achievements by year
- `GET /api/achievements/category/{category}` - Get achievements by category
- `POST /api/achievements` - Create new achievement
- `PUT /api/achievements/{id}` - Update achievement
- `DELETE /api/achievements/{id}` - Delete achievement

### Gallery
- `GET /api/gallery` - Get all gallery items
- `GET /api/gallery/paged` - Get paginated gallery items
- `GET /api/gallery/{id}` - Get gallery item by ID
- `GET /api/gallery/featured` - Get featured gallery items
- `GET /api/gallery/category/{category}` - Get gallery items by category
- `GET /api/gallery/categories` - Get all categories
- `POST /api/gallery` - Create new gallery item
- `PUT /api/gallery/{id}` - Update gallery item
- `DELETE /api/gallery/{id}` - Delete gallery item

### Notifications
- `GET /api/notifications` - Get all notifications
- `GET /api/notifications/unread` - Get unread notifications
- `GET /api/notifications/active` - Get active notifications
- `GET /api/notifications/type/{type}` - Get notifications by type
- `PATCH /api/notifications/{id}/mark-read` - Mark as read
- `PATCH /api/notifications/{id}/mark-unread` - Mark as unread
- `POST /api/notifications` - Create new notification
- `PUT /api/notifications/{id}` - Update notification
- `DELETE /api/notifications/{id}` - Delete notification

### Hero Slides
- `GET /api/hero-slides` - Get all hero slides
- `GET /api/hero-slides/active` - Get active hero slides
- `GET /api/hero-slides/{id}` - Get hero slide by ID
- `POST /api/hero-slides` - Create new hero slide
- `PUT /api/hero-slides/{id}` - Update hero slide
- `DELETE /api/hero-slides/{id}` - Delete hero slide

### Dashboard
- `GET /api/dashboard/home-data` - Get homepage data bundle
- `GET /api/dashboard/statistics` - Get dashboard statistics
- `GET /api/dashboard/recent-activities` - Get recent activities

## Configuration

### Database Configuration

Update `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ieee_vardhaman
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Server Configuration
server.port=8081
spring.application.name=ieee-vardhaman-backend
```

### CORS Configuration

CORS is enabled for all endpoints with `@CrossOrigin(origins = "*")`. For production, configure specific origins:

```java
@CrossOrigin(origins = "http://localhost:5173") // React frontend
```

## Features

### Data Initialization
- Automatic sample data initialization on first startup
- Populates database with societies, councils, events, achievements, and gallery items based on frontend data

### Repository Pattern
- Custom JPA repository methods for complex queries
- Pagination support for large datasets
- Search functionality across multiple fields

### DTO Pattern
- Separation between entities and API responses
- Clean data transfer without exposing internal structure
- Optimized data fetching with selective field inclusion

### Service Layer
- Business logic separation
- Transaction management
- Logging and error handling
- Data validation and transformation

### Advanced Querying
- Custom JPQL queries for complex searches
- Statistical queries for dashboard analytics
- Date range filtering
- Category and tag-based filtering

## Installation & Setup

1. **Prerequisites**
   - Java 17 or higher
   - Maven 3.6+
   - MySQL 8.0+

2. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd demo
   ```

3. **Database Setup**
   ```sql
   CREATE DATABASE ieee_vardhaman;
   ```

4. **Configure Database**
   - Update `application.properties` with your database credentials

5. **Install Dependencies**
   ```bash
   mvn clean install
   ```

6. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

7. **Access API Documentation**
   - Base URL: `http://localhost:8081`
   - Test endpoints using tools like Postman or curl

## Development Guidelines

### Adding New Entities

1. Create entity class in `model` package
2. Create repository interface in `repository` package
3. Create DTO class in `dto` package
4. Create service class in `service` package
5. Create controller class in `controller` package
6. Add data initialization in `DataInitializationService` if needed

### Query Optimization

- Use `@Query` annotations for complex queries
- Implement pagination for large datasets
- Use lazy loading for relationships
- Add database indexes for frequently queried fields

### Error Handling

- Use proper HTTP status codes
- Log errors with appropriate levels
- Return meaningful error messages
- Implement global exception handling

## Testing

The project structure supports:
- Unit tests for services
- Integration tests for repositories
- API tests for controllers

Run tests with:
```bash
mvn test
```

## Deployment

### Production Configuration

1. **Environment Variables**
   - `DATABASE_URL`
   - `DATABASE_USER`
   - `DATABASE_PASSWORD`

2. **Profile-based Configuration**
   - `application-prod.properties`
   - Security configurations
   - Logging levels

3. **Build for Production**
   ```bash
   mvn clean package -Pprod
   ```

## Security

- Spring Security integration ready
- JWT token support structure
- Role-based access control entities
- Password encryption with BCrypt

## Monitoring & Logging

- SLF4J with Logback
- Request/response logging
- Service method execution logging
- Error tracking and reporting

## Future Enhancements

- JWT Authentication implementation
- File upload for images
- Email notification service
- Real-time notifications with WebSocket
- Advanced reporting and analytics
- API documentation with Swagger/OpenAPI

## Contributing

1. Follow the established project structure
2. Write comprehensive tests
3. Use meaningful commit messages
4. Update documentation for new features
5. Follow Java coding conventions

## Support

For technical support or questions, contact the development team or create an issue in the project repository.
