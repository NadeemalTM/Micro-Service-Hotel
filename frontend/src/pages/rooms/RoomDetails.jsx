import { useParams, Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { ArrowLeft } from 'lucide-react';
import { getRoomById } from '../../services/api';

const RoomDetails = () => {
  const { id } = useParams();
  
  const { data, isLoading } = useQuery({
    queryKey: ['room', id],
    queryFn: async () => {
      const response = await getRoomById(id);
      return response.data.data;
    },
  });

  if (isLoading) return <div className="spinner"></div>;

  return (
    <div>
      <Link to="/rooms" className="btn btn-secondary" style={{ marginBottom: '2rem', display: 'inline-flex' }}>
        <ArrowLeft size={20} /> Back
      </Link>
      
      <div className="card">
        <h2>Room {data?.roomNumber}</h2>
        <div style={{ marginTop: '2rem', display: 'grid', gridTemplateColumns: 'repeat(2, 1fr)', gap: '1rem' }}>
          <p><strong>Floor:</strong> {data?.floor}</p>
          <p><strong>Type:</strong> {data?.roomType}</p>
          <p><strong>Capacity:</strong> {data?.capacity} guests</p>
          <p><strong>Bed Type:</strong> {data?.bedType}</p>
          <p><strong>Bed Count:</strong> {data?.bedCount}</p>
          <p><strong>Price/Night:</strong> LKR {parseFloat(data?.pricePerNight || 0).toLocaleString()}</p>
          <p><strong>Size:</strong> {data?.roomSize}m²</p>
          <p><strong>Status:</strong> <span className="badge badge-success">{data?.status}</span></p>
          <p><strong>Balcony:</strong> {data?.hasBalcony ? 'Yes' : 'No'}</p>
          <p><strong>Sea View:</strong> {data?.hasSeaView ? 'Yes' : 'No'}</p>
        </div>
        <div style={{ marginTop: '1.5rem' }}>
          <p><strong>Amenities:</strong> {data?.amenities}</p>
          <p><strong>Description:</strong> {data?.description}</p>
        </div>
      </div>
    </div>
  );
};

export default RoomDetails;
