import React, {Component} from 'react';
import {FormField, Form} from 'react-pattern-library';
import './App.css';
import Row from './Row'
export default class GameBoard extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="square" id="gameBoardContainer">
              {
                this.props.gameBoard.map((x, i) => {
                  return (
                    <div>
                      <Row id={i} changeColor={this.props.changeColor} row={x.row}/>
                    </div>)
                })
              }
            </div>
        )
    }
}