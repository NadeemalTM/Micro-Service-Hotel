import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import { Plus, Eye } from 'lucide-react';
import { getAllRooms } from '../../services/api';

const RoomList = () => {
  const { data, isLoading, error } = useQuery({
    queryKey: ['rooms'],
    queryFn: async () => {
      const response = await getAllRooms();
      return response.data.data;
    },
  });

  if (isLoading) return <div className="spinner"></div>;
  if (error) return <div className="error-message">Error loading rooms</div>;

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>Rooms</h1>
        <Link to="/rooms/add" className="btn btn-primary">
          <Plus size={20} /> Add Room
        </Link>
      </div>

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Room #</th>
              <th>Floor</th>
              <th>Type</th>
              <th>Capacity</th>
              <th>Price/Night</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {data?.map((room) => (
              <tr key={room.id}>
                <td>{room.roomNumber}</td>
                <td>{room.floor}</td>
                <td>{room.roomType}</td>
                <td>{room.capacity} guests</td>
                <td>LKR {parseFloat(room.pricePerNight || 0).toLocaleString()}</td>
                <td>
                  <span className={`badge badge-${room.status === 'AVAILABLE' ? 'success' : room.status === 'OCCUPIED' ? 'danger' : 'warning'}`}>
                    {room.status}
                  </span>
                </td>
                <td>
                  <Link to={`/rooms/${room.id}`} className="btn btn-secondary" style={{ padding: '0.5rem' }}>
                    <Eye size={16} />
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default RoomList;
