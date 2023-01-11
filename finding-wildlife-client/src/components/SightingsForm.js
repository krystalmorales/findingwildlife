import { useHistory, useParams } from 'react-router-dom';
import React, { useRef, useEffect, useState, useContext } from 'react';
import ParkSelection from './ParkSelection';
import mapboxgl from 'mapbox-gl';
import OrganismSelection from './OrganismSelection';
import Error from './Error';
import UserContext from '../UserContext';

mapboxgl.accessToken = 'pk.eyJ1IjoibWFzb250czciLCJhIjoiY2w4aXZlbGI0MWFzcTN3bGcxNWJrdDh1cCJ9.xlQs05-X78HUsdWbz8AZJA';
const DEFAULT_SIGHTING = {date: '', time: '', comments: '', latitude: '', longitude: '', parkId:'', organismId:''  }

function SightingsForm(){

  const history = useHistory();
  const authManager = useContext(UserContext);

  const [parks, setParks] = useState([]);
  const [organisms, setOrganisms] = useState([]);
  const [sighting, setSighting] = useState(DEFAULT_SIGHTING);
  const [errors, setErrors] = useState([]);

  const mapContainer = useRef(null);
  const map = useRef(null);
  const [lng, setLng] = useState(-110.5423379641409);
  const [lat, setLat] = useState(44.56999023015999);
  const [zoom, setZoom] = useState(8);

  useEffect(() => {
    fetch('http://localhost:8080/finding-wildlife/park')
    .then(response => {
      if(response.status === 200) {
        return response.json();
      }
      return Promise.reject('Something Went Wrong!');
    })
    .then(data => {
      setParks(data);
    })
    .catch(error => history.push('/error', {errorMessage: error}));

    map.current = new mapboxgl.Map({
      container: mapContainer.current,
      style: 'mapbox://styles/masonts7/cl8kcn8cs000s14o0i2bcdbo8',
      center: [lng, lat],
      zoom: zoom
      });
      map.current.on("click", handleMapClick);
  }, []);


    let marker = null;

    function handleMapClick(event){
        if(marker){
          marker.remove();
        }

        let coord = event.lngLat
        marker = new mapboxgl.Marker()
        .setLngLat(coord)
        .addTo(map.current);

        setSighting(sighting => {
          return {...sighting,latitude:coord.lat,longitude:coord.lng
          }});
      };

    function handleParkChange(event){
      handleChange(event);
      for(let i = 0; i< parks.length; i++){
        if(parks[i] === event.target.value){
          setLat(parks[i].latitude);
          setLng(parks[i].longitude);
        }
      }

      fetch(`http://localhost:8080/finding-wildlife/park-organism/${event.target.value}`)
      .then(response => {
        if(response.status === 200) {
          return response.json();
        }
        return Promise.reject('Something Went Wrong!');
      })
      .then(data => {
        setOrganisms(data);
      })
      .catch(error => history.push('/error', {errorMessage: error}));
    };

    function addSighting(){
      const init = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${authManager.user.token}`
        },
        body: JSON.stringify({...sighting, appUserId: authManager.user.appUserId})
      };

      fetch(`http://localhost:8080/finding-wildlife/sighting`, init)
      .then(response => {
        if(response.status === 201 || response.status === 400){
          return response.json();
        }
        return Promise.reject('Something Went Wrong');
      })
      .then(body => {
        if(body.sightingId){
          history.push(`/sightings`);
        }
        else if(body){
          setErrors(body);
        }
      })
      .catch(error => history.push('/error', {errorMessage: error}))
    }

    function handleChange(event){
      const property = event.target.name;
      let value = event.target.value;

      const newSighting = {...sighting};

      newSighting[property] = value;
      setSighting(newSighting);
    }

    function onSubmit(event){
      event.preventDefault();

      addSighting();
    }


    function handleCancel(){
      history.push('/');
    }

    return(<>
    <div className='add-sighting-container'>
    <h2>Add Sighting</h2>
    {errors.length > 0 ? <Error errors={errors}/> : null}
      <form name='sightingForm'>
      <div className='form-group'>
          <select className="form-control-sm mb-3" name="parkId" onChange={handleParkChange}>
            <option value="1">Select National Park</option>
                {parks.map(park => <ParkSelection key={park.parkId} park={park}/>)}
            </select>
          </div>
          <div className='form-group'>
          <select className="form-control-sm mb-3" name="organismId" onChange={handleChange}>
            <option defaultValue="1" >Select Organism</option>
              {organisms.map(organism => <OrganismSelection key={organism.organismId} organism={organism}/>)}
            </select>
          </div>
        <div className="form-group">
          <label htmlFor="date"></label>
          <input name="date" type="date" className="form-control-sm mb-3" id="date" onChange={handleChange}/>
        </div>
        <div className="form-group">
          <label htmlFor="time"></label>
          <input name="time" type="time" className="form-control-sm mb-3" id="time" onChange={handleChange}/>
        </div>
        <div className="form-group">
          <label htmlFor="comments"></label>
          <input name="comments" type="text" className="form-control-sm mb-3 comments" id="comments" placeholder="Enter Comments Here" onChange={handleChange}/>
        </div>
      </form>
      <div>
        <div ref={mapContainer} className="map-container add-sighting-map"/>
      </div>
      <div>
      <button type="button" className="btn btn-danger btn-lg btn-block mt-3" onClick={handleCancel}>Cancel</button>
      <button type="submit" className="btn btn-success btn-lg mt-3" onClick={onSubmit}>Submit</button>
      </div>
      </div>
    </>);

}

export default SightingsForm;























{/* <div className="form-group">
<label htmlFor="latitude">Latitude:</label>
<input name="latitude" type="number" className="form-control" id="latitude" onChange={handleChange}/>
</div>
<div className="form-group">
<label htmlFor="longitude">Longitude:</label>
<input name="longitude" type="number" className="form-control" id="longitude" onChange={handleChange}/>
</div> */}