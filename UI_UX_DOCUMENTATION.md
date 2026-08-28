# ODIN UI/UX Documentation

## Overview

ODIN PRO is an enterprise-level security application with a modern, intuitive, and professional interface built with Jetpack Compose and Material Design 3.

## Architecture

### MVVM Pattern

The application follows the Model-View-ViewModel (MVVM) architectural pattern:

- **Model**: Domain models and data repositories
- **View**: Jetpack Compose UI screens
- **ViewModel**: Business logic and state management

### Package Structure

```
com.odin.core/
├── ui/
│   ├── MainActivity.kt
│   ├── theme/              # Material Design 3 theme
│   ├── screens/           # Compose screens
│   │   ├── dashboard/
│   │   ├── logs/
│   │   ├── threats/
│   │   ├── firewall/
│   │   ├── quarantine/
│   │   └── settings/
│   ├── components/        # Reusable UI components
│   ├── viewmodel/         # ViewModels
│   └── navigation/        # Navigation setup
├── data/
│   ├── repository/        # Repository implementations
│   └── database/         # Room database
├── domain/
│   ├── model/             # Domain models
│   └── repository/        # Repository interfaces
├── di/                    # Hilt dependency injection
├── service/               # Background services
└── network/               # Network security components
```

## Screens

### 1. Dashboard (Táctico)

**Purpose**: Provides a tactical overview of system security status.

**Components**:
- SecurityStatusCard: Displays overall security status with threat level
- MetricsGrid: Shows CPU, RAM, posture score, active threats, EDR health
- RecentEventsTimeline: Scrollable timeline of recent security events

**Features**:
- Real-time security status updates
- Component status indicators (VPN, Firewall, Detection)
- Color-coded threat levels
- Refresh functionality

### 2. Logs (Centro de Logs)

**Purpose**: Centralized log viewing and management.

**Components**:
- SearchBar: Real-time search with debouncing
- LogItem: Individual log entries with severity indicators
- FilterDialog: Advanced filtering options
- LogDetailDialog: Detailed log information

**Features**:
- Real-time search
- Filter by severity, source, date range
- Export to PDF, CSV, JSON
- Color-coded severity levels

### 3. Threats (Hunter Pro)

**Purpose**: Threat detection and management center.

**Components**:
- ThreatStatisticsCard: Overview of threat statistics
- ThreatItem: Individual threat entries
- ThreatDetailDialog: Detailed threat information

**Features**:
- Threat level classification (Critical, High, Medium, Low)
- Threat statistics by type
- Dismiss threats functionality
- Real-time threat updates

### 4. Firewall (Centro de Firewall)

**Purpose**: Firewall rule management.

**Components**:
- FirewallRuleItem: Individual rule entries with toggle switches
- CreateRuleDialog: Form for creating new rules
- EditRuleDialog: Form for editing existing rules

**Features**:
- Create, edit, delete firewall rules
- Toggle rules on/off
- Real-time validation
- Rule statistics

### 5. Quarantine (Centro de Cuarentena)

**Purpose**: Application quarantine management.

**Components**:
- QuarantineAppItem: Quarantined application entries
- QuarantineAppDetailDialog: Detailed app information

**Features**:
- View quarantined applications
- Release, delete, or keep in quarantine
- Behavior monitoring
- Risk scoring

### 6. Settings (Ajustes)

**Purpose**: Application configuration.

**Components**:
- SettingItem: Reusable setting component
- SettingSwitch: Setting with toggle switch

**Features**:
- General settings (theme, notifications)
- Security settings (sensitivity, auto-response)
- Network settings (VPN, Tor, DNS, proxy)

## Design System

### Colors

**Primary Colors**:
- Primary: #4CAF50 (Green) - Security/Active states
- Secondary: #2196F3 (Blue) - Informational elements

**Status Colors**:
- Error: #F44336 (Red) - Threats/Critical states
- Warning: #FF9800 (Orange) - Warnings
- Info: #03A9F4 (Light Blue) - Information

**Background Colors**:
- Background: #121212 (Dark theme)
- Surface: #1E1E1E
- Surface Variant: #2D2D2D

### Typography

**Display Styles**:
- Display Large: 57sp
- Display Medium: 45sp
- Display Small: 36sp

**Headline Styles**:
- Headline Large: 32sp
- Headline Medium: 28sp
- Headline Small: 24sp

**Body Styles**:
- Body Large: 16sp
- Body Medium: 14sp
- Body Small: 12sp

### Shapes

- Extra Small: 4dp
- Small: 8dp
- Medium: 12dp
- Large: 16dp
- Extra Large: 24dp

## Navigation

### Bottom Navigation

The application uses a bottom navigation bar with the following tabs:
- Táctico (Dashboard)
- Logs
- Hunter Pro (Threats)
- Firewall
- Ajustes (Settings)

### Navigation Badges

Notification badges are supported for displaying unread counts on navigation items.

## Components

### Reusable Components

1. **SecurityStatusCard**: Displays security status with shield icon
2. **MetricsCard**: Displays metric with icon and value
3. **MetricsGrid**: Grid of metric cards
4. **RecentEventsTimeline**: Timeline of recent events
5. **LogItem**: Individual log entry
6. **ThreatItem**: Individual threat entry
7. **FirewallRuleItem**: Individual firewall rule
8. **QuarantineAppItem**: Individual quarantined app
9. **SettingItem**: Reusable setting component
10. **SettingSwitch**: Setting with toggle switch

## State Management

### ViewModels

All screens use ViewModels for state management:
- DashboardViewModel
- LogsViewModel
- ThreatsViewModel
- FirewallViewModel
- QuarantineViewModel
- SettingsViewModel

### StateFlow

All ViewModels use StateFlow for reactive state management, ensuring UI updates automatically when data changes.

## Integration

### Existing Components

The UI integrates with existing ODIN security components:
- SentryCore: Forensics and logging
- ThreatDetectionEngine: Threat detection
- DynamicRuleEngine: Firewall rules
- QuarantineManager: Application quarantine

### Dependency Injection

Hilt is used for dependency injection, providing:
- Repository implementations
- Service instances
- ViewModel injection

## Performance

### Metrics

Target performance metrics:
- Dashboard load: <500ms (95th percentile)
- Logs load: <300ms (95th percentile)
- Threats load: <300ms (95th percentile)
- Firewall load: <200ms (95th percentile)
- Quarantine load: <200ms (95th percentile)
- Settings load: <100ms (95th percentile)

### Optimization

- LazyColumn for efficient list rendering
- StateFlow for reactive updates
- Coroutines for async operations
- Proper lifecycle management

## Accessibility

### WCAG AA Compliance

- Color contrast ratio: 4.5:1
- Minimum text size: 14sp
- Minimum touch target: 48dp
- Screen reader support: TalkBack
- Content descriptions for interactive elements

## Testing

### Unit Tests

- ViewModel tests
- Repository tests
- Domain model tests

### Integration Tests

- Repository integration
- Service integration
- Database integration

### UI Tests

- Compose UI tests
- Navigation tests
- Component tests

## Future Enhancements

### Planned Features

1. Network Activity Chart: Real-time network traffic visualization
2. Threat Map: Geolocated threat visualization using Google Maps
3. Behavior Monitor: Real-time behavior monitoring visualization
4. Advanced Analytics: Detailed analytics dashboard
5. Custom Themes: User-customizable themes
6. Widget Support: Home screen widgets
7. Watch Integration: Wear OS companion app

### Performance Improvements

- Database optimization
- Caching strategies
- Image loading optimization
- Animation performance tuning

## Build and Deployment

### Build Configuration

- Kotlin DSL for Gradle
- Compose Compiler: 1.5.10
- Kotlin: 1.9.22
- Target SDK: 34
- Min SDK: 26

### Dependencies

Key dependencies include:
- Jetpack Compose BOM: 2024.02.01
- Material Design 3
- Hilt: 2.50
- Navigation Compose: 2.7.7
- Room: 2.6.1
- Coroutines: 1.8.0

## Support

For issues or questions, refer to the main project documentation or contact the development team.
