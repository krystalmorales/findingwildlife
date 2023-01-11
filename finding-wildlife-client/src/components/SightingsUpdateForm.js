import { useHistory, useParams } from 'react-router-dom';
import React, { useRef, useEffect, useState, useContext } from 'react';
import mapboxgl from 'mapbox-gl';
import Error from './Error';
import UserContext from '../UserContext';

mapboxgl.accessToken = 'pk.eyJ1IjoibWFzb250czciLCJhIjoiY2w4aXZlbGI0MWFzcTN3bGcxNWJrdDh1cCJ9.xlQs05-X78HUsdWbz8AZJA';
const DEFAULT_SIGHTING = {date: '', time: '', comments: '', latitude: '', longitude: '', parkId:'', organismId:''  }


function SightingsUpdateForm(){

    let marker = null;

    const history = useHistory();
    const { editId } = useParams();
    const authManager = useContext(UserContext)

    const [sighting, setSighting] = useState(DEFAULT_SIGHTING);
    const [errors, setErrors] = useState([]);

    const mapContainer = useRef(null);
    const map = useRef(null);
    const [lng, setLng] = useState(-110.5423379641409);
    const [lat, setLat] = useState(44.56999023015999);
    const [zoom, setZoom] = useState(9);

    useEffect(() => {
        console.log(editId)
        if(editId) {
            fetch(`http://localhost:8080/finding-wildlife/sighting/sightingId/${editId}`)
            .then(response => {
                switch(response.status){
                    case 200:
                        return response.json();
                    default:
                        return Promise.reject('Something Went Wrong!');
                }
            })
            .then(body => {
                if(body){
                    console.log("body ", body)
                    console.log("body 0 ", body[0])
                    setSighting(body[0]);
                }
                console.log("sighting ", sighting)
            })
            .catch(error => history.push('/error', {errorMessage: error}));
        }

        map.current = new mapboxgl.Map({
            container: mapContainer.current,
            style: 'mapbox://styles/masonts7/cl8kcn8cs000s14o0i2bcdbo8',
            center: [lng, lat],
            zoom: zoom
            });
            map.current.on("click", handleMapClick);
        }, []);


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

    function updateSighting(){
        const updateSighting = {id: editId, ...sighting, appUserId: authManager.user.appUserId};

        const init = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${authManager.user.token}`
            },
            body: JSON.stringify(updateSighting)
        };

        fetch(`http://localhost:8080/finding-wildlife/sighting/${editId}`, init)
        .then(response => {
            switch(response.status) {
                case 204:
                    return null;
                case 400:
                    return response.json();
                case 409:
                    history.push('/not-found', { id: editId });
                    break;
                default:
                    return Promise.reject('Something Went Wrong!');
            }
        })
        .then(body => {
            if(!body) {
                history.push('/userPage')
            }
            else if(body){
                setErrors(body);
            }
        })
        .catch(error => history.push('./error', {errorMessage: error}));
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

        updateSighting();
    }

    function handleCancel(){
        history.push('/');
    }

    return(<>
    <div className='update-sighting-container'>
        <h2 className='center-text'>Update Sighting</h2>
        {errors.length > 0 ? <Error errors={errors}/> : null}
        <form name='sightingForm' onSubmit={onSubmit}>
            <div className="form-group">
            <label htmlFor="date">Date:</label>
            <input name="date" type="date" className="form-control mb-3" id="date" onChange={handleChange}/>
            </div>
            <div className="form-group">
            <label htmlFor="time">Time:</label>
            <input name="time" type="time" className="form-control mb-3" id="time" onChange={handleChange}/>
            </div>
            <div className="form-group">
            <label htmlFor="comments">Comments:</label>
            <input name="comments" type="text" className="form-control mb-3" id="comments" onChange={handleChange}/>
            </div>
            <div className="form-group">
            </div>
        </form>
        <div>
            <div ref={mapContainer} className="map-container update-sighting-map"/>
        </div>
        <button type="submit" className="btn btn-success mt-3">Submit</button>
        <button type="button" className="btn btn-danger mt-3" onClick={handleCancel}>Cancel</button>
        </div>
    </>)

}

export default SightingsUpdateForm;