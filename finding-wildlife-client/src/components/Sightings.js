import { useHistory, useParams } from 'react-router-dom';
import React, { useRef, useEffect, useState } from 'react';
import mapboxgl from 'mapbox-gl';
import ParkSelection from './ParkSelection';

mapboxgl.accessToken = 'pk.eyJ1IjoibWFzb250czciLCJhIjoiY2w4aXZlbGI0MWFzcTN3bGcxNWJrdDh1cCJ9.xlQs05-X78HUsdWbz8AZJA';

const park = 
[{parkId: 1, parkName: 'Yellowstone', lat: 44.42, lng: -110.5885, comments: "Testing"},
{parkId: 2, parkName: 'Big Bend', lat: 45.42, lng: -111.5885, comments: "Test"},
{parkId: 3, parkName: 'White Sands', lat: 45.42, lng: -109.5885, comments: "Testing123"}];


function Sightings(){

    let parkSelected = 0;
    let marker = null;

    const history = useHistory();

    const [parks, setParks] = useState([]);
    const [sightings, setSightings] = useState([]);

    const mapContainer = useRef(null);
    const map = useRef(null);
    const [lng, setLng] = useState(-110.5423379641409);
    const [lat, setLat] = useState(44.56999023015999);
    const [zoom, setZoom] = useState(9);

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

      if (map.current) return;
      map.current = new mapboxgl.Map({
      container: mapContainer.current,
      style: 'mapbox://styles/masonts7/cl8kcn8cs000s14o0i2bcdbo8',
      center: [lng, lat],
      zoom: zoom
      });
    }, []);

    function handleParkChange(event){
      parkSelected = event.target.value;
      for(let x = 0; x < sightings.length; x++){
        if(park[x].parkId === parkSelected){
          setLat(park[x].latitude);
          setLng(park[x].longitude);
          break;
        }
      }

      const today = "2022-10-05";
      const yesterday = "2022-10-04";
      fetch(`http://localhost:8080/finding-wildlife/sighting/date/${yesterday}/${today}/${parkSelected}`)
      .then(response => {
        switch(response.status) {
          case 200:
            return response.json();
          case 404:
            history.push('/not-found')
            break;
          default:
            return Promise.reject('Something Went Wrong!');
        }
      })
      .then(body => {
        if(body){
          setSightings(body);
        }
      })
      .catch(error => history.push('/error', {errorMessage: error}))

    }

    function handleSubmit(event){
      event.preventDefault();

      for(let i = 0; i< sightings.length; i++){
        let popup = new mapboxgl.Popup({ offset: 25 })
        .setHTML(`<h5>${sightings[i].organism.commonName}</h5>
                <h6>${sightings[i].date} @ ${sightings[i].time}<h6>
                <p>${sightings[i].comments}<p>`);
        marker = new mapboxgl.Marker()
        .setLngLat([sightings[i].longitude, sightings[i].latitude])

        .setPopup(popup)
        .addTo(map.current);
      }
    }

    return(<>
    <div className='sightings-container'>
      <h2>View Sightings</h2>
        <form >
        <div className='form-group-row'>
          <select className="form-control-sm mb-3" id="inlineFormCustomSelect" onChange={handleParkChange}>
            <option value="1" >Select National Park</option>
              {parks.map(park => <ParkSelection key={park.parkId} park={park}/>)}
            </select>
          </div>
          <div className="sightings-submit">
            <button type="submit" className="btn btn-success mb-3" onClick={handleSubmit}>Submit</button>
          </div>
        </form>
          <div ref={mapContainer} className="map-container map-sightings"/>
        </div>
    </>);
}

export default Sightings;
