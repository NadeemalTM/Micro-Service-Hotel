import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { Plus, Eye, Calendar, MapPin, Users } from 'lucide-react';
import { getAllEvents } from '../../services/api';

const EventList = () => {
  const { data: events, isLoading } = useQuery({
    queryKey: ['events'],
    queryFn: async () => {
      const response = await getAllEvents();
      return response.data.data;
    },
  });

  const getStatusBadge = (status) => {
    const colors = {
      PLANNED: { background: '#dbeafe', color: '#1e40af' },
      CONFIRMED: { background: '#d1fae5', color: '#065f46' },
      IN_PROGRESS: { background: '#fef3c7', color: '#92400e' },
      COMPLETED: { background: '#e5e7eb', color: '#374151' },
      CANCELLED: { background: '#fee2e2', color: '#991b1b' }
    };
    return (
      <span style={{ padding: '4px 12px', borderRadius: '12px', fontSize: '0.875rem', fontWeight: '500', ...colors[status] }}>
        {status}
      </span>
    );
  };

  const getEventTypeBadge = (type) => {
    const colors = {
      WEDDING: '#fce7f3',
      CONFERENCE: '#dbeafe',
      PARTY: '#fef3c7',
      MEETING: '#e0e7ff',
      SEMINAR: '#e0f2fe',
      EXHIBITION: '#f3e8ff',
      OTHER: '#e5e7eb'
    };
    return (
      <span style={{ padding: '4px 12px', borderRadius: '12px', fontSize: '0.875rem', background: colors[type] || colors.OTHER }}>
        {type}
      </span>
    );
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', { 
      year: 'numeric', 
      month: 'short', 
      day: 'numeric' 
    });
  };

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>Event Management</h1>
        <Link to="/events/add" className="btn btn-primary">
          <Plus size={20} /> Add Event
        </Link>
      </div>

      {isLoading ? (
        <div className="card"><div className="spinner"></div></div>
      ) : events && events.length > 0 ? (
        <div className="card">
          <div style={{ overflowX: 'auto' }}>
            <table>
              <thead>
                <tr>
                  <th>Event Name</th>
                  <th>Type</th>
                  <th>Venue</th>
                  <th>Date</th>
                  <th>Attendees</th>
                  <th>Contact Person</th>
                  <th>Cost (LKR)</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {events.map((event) => (
                  <tr key={event.id}>
                    <td>
                      <strong>{event.eventName || event.name}</strong>
                      <br />
                      <small style={{ color: '#64748b' }}>{event.eventCode || `EVT-${event.id}`}</small>
                    </td>
                    <td>{getEventTypeBadge(event.eventType || event.type)}</td>
                    <td>
                      <MapPin size={14} style={{ display: 'inline', marginRight: '4px' }} />
                      {event.venue?.replace(/_/g, ' ') || 'N/A'}
                    </td>
                    <td>
                      <Calendar size={14} style={{ display: 'inline', marginRight: '4px' }} />
                      {formatDate(event.startDate || event.eventDate)}
                    </td>
                    <td>
                      <Users size={14} style={{ display: 'inline', marginRight: '4px' }} />
                      {event.expectedAttendees || event.attendees || 0}
                    </td>
                    <td>
                      {event.contactPerson || 'N/A'}
                      <br />
                      <small style={{ color: '#64748b' }}>{event.contactPhone || ''}</small>
                    </td>
                    <td>{parseFloat(event.estimatedCost || event.totalCost || 0).toLocaleString()}</td>
                    <td>{getStatusBadge(event.status || 'PLANNED')}</td>
                    <td>
                      <button className="btn btn-secondary" style={{ padding: '0.5rem', fontSize: '0.875rem' }}>
                        <Eye size={16} /> View
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <div className="card">
          <p style={{ textAlign: 'center', color: '#64748b' }}>No events found. Create your first event!</p>
        </div>
      )}
    </div>
  );
};

export default EventList;
