#!/usr/bin/env python3

from graphviz import Digraph

def create_architecture_diagram():
    
    dot = Digraph(comment='NewsApp Architecture', format='png')
    dot.attr(rankdir='TB', splines='polyline', nodesep='0.8', ranksep='1.5')
    dot.attr('node', shape='box', style='rounded,filled', fontname='Arial', fontsize='10')
    
    with dot.subgraph(name='cluster_ui') as c:
        c.attr(label='UI Layer (Presentation)', style='filled', color='lightblue', fontsize='12', fontname='Arial Bold')
        c.node('MA', 'MainActivity', fillcolor='#e1f5ff', color='#01579b', penwidth='2')
        c.node('NF', 'NewsFragment', fillcolor='#e1f5ff', color='#01579b', penwidth='2')
        c.node('BF', 'BlacklistFragment', fillcolor='#e1f5ff', color='#01579b', penwidth='2')
        c.node('ADF', 'ArticleDetailFragment', fillcolor='#e1f5ff', color='#01579b', penwidth='2')
        c.node('NVM', 'NewsViewModel', fillcolor='#e1f5ff', color='#01579b', penwidth='2')
        c.node('BVM', 'BlacklistViewModel', fillcolor='#e1f5ff', color='#01579b', penwidth='2')
        c.node('NA', 'NewsAdapter', fillcolor='#e1f5ff', color='#01579b', penwidth='2')
        c.node('BA', 'BlacklistAdapter', fillcolor='#e1f5ff', color='#01579b', penwidth='2')
        c.node('ACV', 'ArticleCardView', fillcolor='#e1f5ff', color='#01579b', penwidth='2')
    
    with dot.subgraph(name='cluster_domain') as c:
        c.attr(label='Domain Layer (Business Logic)', style='filled', color='#f3e5f5', fontsize='12', fontname='Arial Bold')
        c.node('AR', 'ArticleRepository\n(Interface)', fillcolor='#f3e5f5', color='#4a148c', penwidth='2')
        c.node('UR', 'UserRepository\n(Interface)', fillcolor='#f3e5f5', color='#4a148c', penwidth='2')
        c.node('AM', 'Article Model', fillcolor='#f3e5f5', color='#4a148c', penwidth='2')
        c.node('UM', 'User Model', fillcolor='#f3e5f5', color='#4a148c', penwidth='2')
        c.node('RM', 'Result Model', fillcolor='#f3e5f5', color='#4a148c', penwidth='2')
    
    with dot.subgraph(name='cluster_data') as c:
        c.attr(label='Data Layer', style='filled', color='#e8f5e9', fontsize='12', fontname='Arial Bold')
        c.node('NAR', 'NetworkArticleRepository', fillcolor='#e8f5e9', color='#1b5e20', penwidth='2')
        c.node('IUR', 'InMemoryUserRepository', fillcolor='#e8f5e9', color='#1b5e20', penwidth='2')
        c.node('RC', 'RetrofitClient', fillcolor='#e8f5e9', color='#1b5e20', penwidth='2')
        c.node('NAS', 'NewsApiService', fillcolor='#e8f5e9', color='#1b5e20', penwidth='2')
    
    with dot.subgraph(name='cluster_external') as c:
        c.attr(label='External Services', style='filled', color='#fff3e0', fontsize='12', fontname='Arial Bold')
        c.node('API', 'News API\n(newsapi.org)', fillcolor='#fff3e0', color='#e65100', penwidth='2')
    
    dot.edge('MA', 'NF')
    dot.edge('MA', 'BF')
    dot.edge('MA', 'ADF')
    
    dot.edge('NF', 'NVM')
    dot.edge('NF', 'NA')
    
    dot.edge('BF', 'BVM')
    dot.edge('BF', 'BA')
    
    dot.edge('NA', 'ACV')
    
    dot.edge('NVM', 'AR')
    dot.edge('NVM', 'UR')
    
    dot.edge('BVM', 'UR')
    
    dot.edge('AR', 'NAR', style='dashed', label='implements')
    dot.edge('UR', 'IUR', style='dashed', label='implements')
    
    dot.edge('AR', 'AM')
    dot.edge('AR', 'RM')
    dot.edge('UR', 'UM')
    
    dot.edge('NAR', 'RC')
    dot.edge('RC', 'NAS')
    dot.edge('NAS', 'API')
    dot.edge('IUR', 'UM')
    
    return dot

if __name__ == '__main__':
    diagram = create_architecture_diagram()
    
    output_file = 'architecture-diagram'
    diagram.render(output_file, cleanup=True)
    print(f'Architecture diagram saved to {output_file}.png')
    
    with open(f'{output_file}.dot', 'w') as f:
        f.write(diagram.source)
    print(f'.dot file saved to {output_file}.dot')