import dash_bootstrap_components as dbc
import pandas as pd
import plotly.express as px
import dash
import plotly.graph_objs as go
import csv
from dash import Dash, html, dcc, callback, Output, Input


df = pd.read_csv('data.csv')

app = dash.Dash(__name__)

unique_actions = df['action'].unique()

initial_options = [{'label': day, 'value': day} for day in df['day'].unique()]

app.layout = html.Div([
    html.H1(children='Fall Detective - Visualização dos dados', style={'textAlign':'center', 'padding-bottom': '20px', 'color': '#1A4A94'}),
    dbc.Row(
        dbc.Col([
            html.H3(children=" Selecione a ação", style={'color':'#0A2A5B'}),
            html.Div(
                dbc.RadioItems(
                    id="action-selector",
                    className="btn-group",
                    inputClassName="btn-check",
                    labelClassName="btn btn-outline-dark",
                    labelCheckedClassName="active",
                    options=[{'label': action.capitalize(), 'value': action} for action in unique_actions],
                    value=unique_actions[0],
                    style={'margin':'.5rem', 'position':'relative', 'cursor':'pointer', 'font-family':'Arial'}
                )
            ),
        ])
    ),
    dbc.Row(
        [
            dbc.Col(
                [
                    html.H3(children="Selecione o dia", style={'color':'#0A2A5B'}),
                    dcc.Dropdown(
                        id='day-selector',
                        options=initial_options,
                        value=initial_options[0]['value']
                    )
                ],
                width=6
            ),
            dbc.Col(
                [
                    html.H3(children="Selecione o ID", style={'color':'#0A2A5B'}),
                    dcc.Dropdown(id='id-selector')
                ],
                width=6
            )
        ]
    ),
    dbc.Row([
            dbc.Col([
                dcc.Graph(id='graph-eixos')
            ]),
            dbc.Col([
                dcc.Graph(id='graph-magnitude')
            ]),
        ]),
    ])

def update_day_options(selected_action):
    filtered_df = df[df['action'] == selected_action]
    days = filtered_df['day'].unique()
    options = [{'label': day, 'value': day} for day in days]
    return options

@app.callback(
    [Output('day-selector', 'options'),
     Output('day-selector', 'value')],
    [Input('action-selector', 'value')]
)

def update_day_selector(selected_action):
    options = update_day_options(selected_action)
    value = options[0]['value'] if options else None
    return options, value

@app.callback(
    Output('id-selector', 'options'),
    [Input('action-selector', 'value'),
     Input('day-selector', 'value')]
)
def update_id_selector(selected_action, selected_day):
    filtered_df = df[(df['action'] == selected_action) & (df['day'] == selected_day)]
    ids = filtered_df['id'].unique()
    options = [{'label': id, 'value': id} for id in ids]
    return options


@app.callback(
    [Output('graph-eixos', 'figure'),
     Output('graph-magnitude', 'figure')],
    [Input('action-selector', 'value'),
     Input('day-selector', 'value'),
     Input('id-selector', 'value')]
)

def update_graphs(selected_action, selected_day, selected_id):
    # Filter DataFrame based on sellected action, day and id
    filtered_df = df[(df['action'] == selected_action) & (df['day'] == selected_day) & (df['id'] == selected_id)]
    write_to_csv_eixos('eixos.csv', filtered_df)
    aux_df1 = pd.read_csv('eixos.csv')
    
    fig_eixos = px.line(aux_df1, x='timestamp', y=['valueX', 'valueY', 'valueZ'], title='Gráfico de Eixos')
    
    fig_eixos.update_layout(
    title=dict(
        text="Gráfico dos Eixos",
        x=0.5, 
        xanchor="center" 
    ))
    
    write_to_csv_mag('mag.csv', filtered_df)
    aux_df2 = pd.read_csv('mag.csv')


    fig_magnitude = {
        "data": [
            {
                "x": aux_df2["timestamp"],
                "y": aux_df2["magnitude"],
                "type": "line",
                "name": "Magnitude",
            }
        ],
        "layout": {
            "title": "Gráfico da Magnitude",
            "xaxis": {"title": "timestamp"},
            "yaxis": {"title": "magnitude"},
        },
    }

    return fig_eixos, fig_magnitude

def write_to_csv_mag(csv_name, df):
    with open(csv_name, 'w', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(['timestamp', 'magnitude'])
        
        timestamps_list = [eval(timestamp_str) for timestamp_str in df['timestamp']]
        magnitudes_list = [eval(magnitude_str) for magnitude_str in df['magnitude']]

        
        for timestamps, magnitudes in zip(timestamps_list, magnitudes_list):
            for timestamp, magnitude in zip(timestamps, magnitudes):
                writer.writerow([timestamp, magnitude])



def write_to_csv_eixos(csv_name, df):
    with open(csv_name, 'w', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(['timestamp', 'valueX', 'valueY', 'valueZ'])
        
        timestamps_list = [eval(timestamp_str) for timestamp_str in df['timestamp']]
        eixoX_list = [eval(magnitude_str) for magnitude_str in df['valueX']]
        eixoY_list = [eval(magnitude_str) for magnitude_str in df['valueY']]
        eixoZ_list = [eval(magnitude_str) for magnitude_str in df['valueZ']]

        
        for timestamps, xs, ys, zs in zip(timestamps_list, eixoX_list, eixoY_list, eixoZ_list):
            for timestamp, x,y,z in zip(timestamps, xs,ys,zs):
                writer.writerow([timestamp, x,y,z])


if __name__ == '__main__':
    app.run(debug=True)
