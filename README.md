# Readme

## FASTA Sequence

This is a Python script that reads in a FASTA sequence file and calculates the sequence length, GC percentage, and the number of times the substring "ATAT" occurs in the DNA sequence.

### Usage

To use the script, provide the path to a FASTA file as an argument, like this:

python exercise1.py <path_to_fasta>

Replace <path_to_fasta> with the appropriate file path.

The script will then print out the following information:

    The sequence length
    The GC percentage
    The number of times the substring "ATAT" occurs in the sequence

### Tests

To run tests for the script, you can create a few sample FASTA files and run the script with these files as arguments. For example, create a file called test.fasta with the following contents:

>Test sequence
ATCGATCGATCGATCGATCGATCGATCGATCG

You can run the script with the command python exercise1.py test.fasta and verify that it calculates the correct sequence length, GC percentage, and the number of times the substring "ATAT" occurs in the sequence.

### sbatch 
To execute the batch we use the next command 
sbatch exercise1.slrm

#### sbatch output
SLURM_JOB_ID: 60085525
SLURM_JOB_USER: vsc35171
SLURM_JOB_ACCOUNT: lp_edu_large_omics
SLURM_JOB_NAME: exercise1.slrm
SLURM_CLUSTER_NAME: wice
SLURM_JOB_PARTITION: batch
SLURM_NNODES: 1
SLURM_NODELIST: m33c32n4
SLURM_JOB_CPUS_PER_NODE: 1
Date: Thu Mar 16 21:49:18 CET 2023
Walltime: 00-00:04:00

Thu Mar 16 21:49:18 CET 2023
#/lustre1/project/stg_00079/teaching/data/keratin.fasta
seq_len  gc_per          ATAT      
1190     58              2         
Thu Mar 16 21:49:18 CET 2023
